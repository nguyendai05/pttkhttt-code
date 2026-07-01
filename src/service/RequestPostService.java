package service;

import model.Comment;
import model.DocumentItem;
import model.RequestPost;
import model.UserAccount;
import model.enums.DocumentStatus;
import model.enums.RequestStatus;
import model.enums.Role;
import repository.CommentRepository;
import repository.DocumentRepository;
import repository.RequestPostRepository;
import util.IdGenerator;
import util.InputValidator;
import util.OperationResult;

import java.util.List;

/**
 * OWNER: Tạ Văn Huy
 * FEATURE GROUP: Forum yêu cầu tài liệu
 * RELATED USE CASES: UC-10a, UC-10b
 * PURPOSE: Tạo/quản lý bài yêu cầu của User và duyệt bài yêu cầu bởi Admin.
 */
public class RequestPostService {
    private final RequestPostRepository requestPostRepository;
    private final DocumentRepository documentRepository;
    private final CommentRepository commentRepository;
    private final ActivityLogService activityLogService;
    private final SessionManager sessionManager;

    public RequestPostService(RequestPostRepository requestPostRepository, DocumentRepository documentRepository,
                              CommentRepository commentRepository, ActivityLogService activityLogService,
                              SessionManager sessionManager) {
        this.requestPostRepository = requestPostRepository;
        this.documentRepository = documentRepository;
        this.commentRepository = commentRepository;
        this.activityLogService = activityLogService;
        this.sessionManager = sessionManager;
    }

    /**
     * OWNER: Tạ Văn Huy
     * USE CASE: UC-10a - Tạo bài yêu cầu tài liệu
     * ACTOR: User
     * FLOW: Basic Flow / Exception Flow
     * PURPOSE: User tạo RequestPost mới ở trạng thái PENDING_REVIEW.
     * SEQUENCE NOTE: ConsoleView -> RequestPostController -> RequestPostService -> RequestPostRepository -> SessionManager.
     */
    public OperationResult<RequestPost> createPost(String title, String content) {
        UserAccount user = requireUser();
        if (user == null) {
            return OperationResult.fail("Chỉ User đã đăng nhập được tạo bài yêu cầu.");
        }
        if (InputValidator.isBlank(title) || InputValidator.isBlank(content)) {
            return OperationResult.fail("Title và content không được trống.");
        }
        RequestPost post = new RequestPost(IdGenerator.nextId("REQ"), title.trim(), content.trim(), user.id, RequestStatus.PENDING_REVIEW);
        requestPostRepository.save(post);
        return OperationResult.ok("Tạo bài yêu cầu thành công. Bài đang chờ Admin duyệt.", post);
    }

    /**
     * OWNER: Tạ Văn Huy
     * USE CASE: UC-10a - Xem bài yêu cầu của tôi
     * ACTOR: User
     * FLOW: Basic Flow
     * PURPOSE: User xem danh sách bài yêu cầu do chính mình tạo.
     * SEQUENCE NOTE: ConsoleView -> RequestPostController -> RequestPostService -> RequestPostRepository -> SessionManager.
     */
    public OperationResult<List<RequestPost>> myPosts() {
        UserAccount user = requireUser();
        if (user == null) {
            return OperationResult.fail("Chỉ User đã đăng nhập được xem bài của mình.");
        }
        return OperationResult.ok("Bài yêu cầu của tôi.", requestPostRepository.findByCreator(user.id));
    }

    /**
     * OWNER: Tạ Văn Huy
     * USE CASE: UC-10a - Sửa bài yêu cầu của tôi
     * ACTOR: User
     * FLOW: Alternative Flow / Exception Flow
     * PURPOSE: User sửa bài của chính mình nếu bài chưa FULFILLED/HIDDEN/DELETED; bài OPEN chuyển lại PENDING_REVIEW.
     * SEQUENCE NOTE: ConsoleView -> RequestPostController -> RequestPostService -> RequestPostRepository -> SessionManager.
     */
    public OperationResult<RequestPost> updateOwnPost(String postId, String title, String content) {
        UserAccount user = requireUser();
        RequestPost post = requestPostRepository.findById(postId).orElse(null);
        if (user == null || post == null || !post.creatorId.equals(user.id)) {
            return OperationResult.fail("Chỉ chủ bài được sửa bài yêu cầu.");
        }
        if (post.status == RequestStatus.FULFILLED || post.status == RequestStatus.HIDDEN || post.status == RequestStatus.DELETED) {
            return OperationResult.fail("Bài hiện tại không còn hợp lệ để sửa.");
        }
        if (InputValidator.isBlank(title) || InputValidator.isBlank(content)) {
            return OperationResult.fail("Title và content không được trống.");
        }
        post.title = title.trim();
        post.content = content.trim();
        if (post.status == RequestStatus.OPEN) {
            post.status = RequestStatus.PENDING_REVIEW;
        }
        requestPostRepository.save(post);
        return OperationResult.ok("Sửa bài yêu cầu thành công.", post);
    }

    /**
     * OWNER: Tạ Văn Huy
     * USE CASE: UC-10a - Xóa bài yêu cầu của tôi
     * ACTOR: User
     * FLOW: Alternative Flow / Exception Flow
     * PURPOSE: User xóa mềm bài của chính mình nếu bài còn hợp lệ.
     * SEQUENCE NOTE: ConsoleView -> RequestPostController -> RequestPostService -> RequestPostRepository -> SessionManager.
     */
    public OperationResult<RequestPost> deleteOwnPost(String postId) {
        UserAccount user = requireUser();
        RequestPost post = requestPostRepository.findById(postId).orElse(null);
        if (user == null || post == null || !post.creatorId.equals(user.id)) {
            return OperationResult.fail("Chỉ chủ bài được xóa bài yêu cầu.");
        }
        if (post.status == RequestStatus.FULFILLED) {
            return OperationResult.fail("Bài FULFILLED không được xóa trong mô phỏng này.");
        }
        post.deleted = true;
        post.status = RequestStatus.DELETED;
        requestPostRepository.save(post);
        return OperationResult.ok("Xóa bài yêu cầu thành công.", post);
    }

    /**
     * OWNER: Tạ Văn Huy
     * USE CASE: UC-10a - Bình luận vào bài OPEN
     * ACTOR: User
     * FLOW: Alternative Flow / Exception Flow
     * PURPOSE: User bình luận một cấp vào bài yêu cầu đang OPEN.
     * SEQUENCE NOTE: ConsoleView -> RequestPostController -> RequestPostService -> CommentRepository/RequestPostRepository -> SessionManager.
     */
    public OperationResult<Comment> commentOpenPost(String postId, String content) {
        UserAccount user = requireUser();
        if (user == null) {
            return OperationResult.fail("Chỉ User đã đăng nhập được bình luận bài yêu cầu.");
        }
        RequestPost post = requestPostRepository.findById(postId).orElse(null);
        if (post == null || post.status != RequestStatus.OPEN || post.hidden || post.deleted) {
            return OperationResult.fail("Chỉ được bình luận vào bài OPEN đang hiển thị.");
        }
        if (InputValidator.isBlank(content)) {
            return OperationResult.fail("Nội dung bình luận không được trống.");
        }
        Comment comment = new Comment(IdGenerator.nextId("COM"), user.id, "REQUEST", post.id, content.trim());
        commentRepository.save(comment);
        return OperationResult.ok("Bình luận vào bài yêu cầu thành công.", comment);
    }

    /**
     * OWNER: Tạ Văn Huy
     * USE CASE: UC-10a - Đánh dấu yêu cầu đã đáp ứng
     * ACTOR: User
     * FLOW: Alternative Flow / Exception Flow
     * PURPOSE: Chủ bài chuyển bài OPEN sang FULFILLED và có thể liên kết documentId APPROVED.
     * SEQUENCE NOTE: ConsoleView -> RequestPostController -> RequestPostService -> RequestPostRepository/DocumentRepository -> SessionManager.
     */
    public OperationResult<RequestPost> markFulfilled(String postId, String documentId) {
        UserAccount user = requireUser();
        RequestPost post = requestPostRepository.findById(postId).orElse(null);
        if (user == null || post == null || !post.creatorId.equals(user.id)) {
            return OperationResult.fail("Chỉ chủ bài được đánh dấu FULFILLED.");
        }
        if (post.status != RequestStatus.OPEN) {
            return OperationResult.fail("Chỉ bài OPEN mới được đánh dấu FULFILLED.");
        }
        if (!InputValidator.isBlank(documentId)) {
            DocumentItem document = documentRepository.findById(documentId).orElse(null);
            if (document == null || document.status != DocumentStatus.APPROVED) {
                return OperationResult.fail("documentId liên kết phải là tài liệu APPROVED.");
            }
            post.linkedDocumentId = documentId.trim();
        }
        post.status = RequestStatus.FULFILLED;
        requestPostRepository.save(post);
        return OperationResult.ok("Đánh dấu bài yêu cầu FULFILLED thành công.", post);
    }

    /**
     * OWNER: Tạ Văn Huy
     * USE CASE: UC-10b - Duyệt bài yêu cầu tài liệu
     * ACTOR: Admin
     * FLOW: Basic Flow
     * PURPOSE: Admin chuyển bài PENDING_REVIEW sang OPEN để hiển thị công khai và ghi ActivityLog.
     * SEQUENCE NOTE: ConsoleView -> RequestPostController -> RequestPostService -> RequestPostRepository/ActivityLogRepository -> SessionManager.
     */
    public OperationResult<RequestPost> approvePost(String postId) {
        UserAccount admin = requireAdmin();
        if (admin == null) {
            return OperationResult.fail("Chỉ Admin được duyệt bài yêu cầu.");
        }
        RequestPost post = requestPostRepository.findById(postId).orElse(null);
        if (post == null || post.status != RequestStatus.PENDING_REVIEW) {
            return OperationResult.fail("Bài không tồn tại hoặc không ở trạng thái PENDING_REVIEW.");
        }
        post.status = RequestStatus.OPEN;
        requestPostRepository.save(post);
        activityLogService.log(admin.id, "APPROVE_REQUEST", "REQUEST", post.id, "Admin duyệt bài yêu cầu tài liệu.");
        return OperationResult.ok("Duyệt bài yêu cầu thành công.", post);
    }

    /**
     * OWNER: Tạ Văn Huy
     * USE CASE: UC-10b - Từ chối bài yêu cầu tài liệu
     * ACTOR: Admin
     * FLOW: Alternative Flow / Exception Flow
     * PURPOSE: Admin chuyển bài PENDING_REVIEW sang REJECTED, bắt buộc lý do và ghi ActivityLog.
     * SEQUENCE NOTE: ConsoleView -> RequestPostController -> RequestPostService -> RequestPostRepository/ActivityLogRepository -> SessionManager.
     */
    public OperationResult<RequestPost> rejectPost(String postId, String reason) {
        UserAccount admin = requireAdmin();
        if (admin == null) {
            return OperationResult.fail("Chỉ Admin được từ chối bài yêu cầu.");
        }
        if (InputValidator.isBlank(reason)) {
            return OperationResult.fail("Lý do từ chối là bắt buộc.");
        }
        RequestPost post = requestPostRepository.findById(postId).orElse(null);
        if (post == null || post.status != RequestStatus.PENDING_REVIEW) {
            return OperationResult.fail("Bài không tồn tại hoặc không ở trạng thái PENDING_REVIEW.");
        }
        post.status = RequestStatus.REJECTED;
        post.rejectionReason = reason.trim();
        requestPostRepository.save(post);
        activityLogService.log(admin.id, "REJECT_REQUEST", "REQUEST", post.id, reason.trim());
        return OperationResult.ok("Từ chối bài yêu cầu thành công.", post);
    }

    /**
     * OWNER: Tạ Văn Huy
     * USE CASE: UC-10b - Hiển thị bài yêu cầu công khai
     * ACTOR: User/Guest/Admin/Moderator
     * FLOW: Basic Flow
     * PURPOSE: Trả về các bài OPEN/FULFILLED để hiển thị công khai trên forum.
     * SEQUENCE NOTE: ConsoleView -> RequestPostController -> RequestPostService -> RequestPostRepository -> SessionManager.
     */
    public List<RequestPost> publicPosts() {
        return requestPostRepository.findPublicPosts();
    }

    /**
     * OWNER: Tạ Văn Huy
     * USE CASE: UC-10b - Xem bài yêu cầu chờ duyệt
     * ACTOR: Admin
     * FLOW: Basic Flow / Exception Flow
     * PURPOSE: Admin xem danh sách bài PENDING_REVIEW để duyệt hoặc từ chối.
     * SEQUENCE NOTE: ConsoleView -> RequestPostController -> RequestPostService -> RequestPostRepository -> SessionManager.
     */
    public OperationResult<List<RequestPost>> pendingPosts() {
        if (requireAdmin() == null) {
            return OperationResult.fail("Chỉ Admin được xem bài yêu cầu chờ duyệt.");
        }
        return OperationResult.ok("Danh sách bài yêu cầu PENDING_REVIEW.", requestPostRepository.findByStatus(RequestStatus.PENDING_REVIEW));
    }

    private UserAccount requireUser() {
        UserAccount actor = sessionManager.getCurrentUser().orElse(null);
        return actor != null && actor.role == Role.USER ? actor : null;
    }

    private UserAccount requireAdmin() {
        UserAccount actor = sessionManager.getCurrentUser().orElse(null);
        return actor != null && actor.role == Role.ADMIN ? actor : null;
    }
}
