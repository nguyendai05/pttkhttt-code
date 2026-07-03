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


public class RequestPostService {
    private RequestPostRepository requestPostRepository;
    private DocumentRepository documentRepository;
    private CommentRepository commentRepository;
    private ActivityLogService activityLogService;
    private SessionManager sessionManager;

    public RequestPostService(RequestPostRepository requestPostRepository, DocumentRepository documentRepository,
                              CommentRepository commentRepository, ActivityLogService activityLogService,
                              SessionManager sessionManager) {
        this.requestPostRepository = requestPostRepository;
        this.documentRepository = documentRepository;
        this.commentRepository = commentRepository;
        this.activityLogService = activityLogService;
        this.sessionManager = sessionManager;
    }



    public OperationResult<RequestPost> createPost(String title, String content) {
        UserAccount user = requireUser();
        if (user == null) {
            return OperationResult.fail("Chỉ User đã đăng nhập được tạo bài yêu cầu.");
        }
        if (InputValidator.isBlank(title) || InputValidator.isBlank(content)) {
            return OperationResult.fail("Title và content không được trống.");
        }
        RequestPost post = new RequestPost(IdGenerator.nextId("REQ"), title.trim(), content.trim(), user.getId(), RequestStatus.PENDING_REVIEW);
        requestPostRepository.save(post);
        return OperationResult.ok("Tạo bài yêu cầu thành công. Bài đang chờ Admin duyệt.", post);
    }



    public OperationResult<List<RequestPost>> myPosts() {
        UserAccount user = requireUser();
        if (user == null) {
            return OperationResult.fail("Chỉ User đã đăng nhập được xem bài của mình.");
        }
        return OperationResult.ok("Bài yêu cầu của tôi.", requestPostRepository.findByCreator(user.getId()));
    }



    public OperationResult<RequestPost> updateOwnPost(String postId, String title, String content) {
        UserAccount user = requireUser();
        RequestPost post = requestPostRepository.findById(postId).orElse(null);
        if (user == null || post == null || !post.getCreatorId().equals(user.getId())) {
            return OperationResult.fail("Chỉ chủ bài được sửa bài yêu cầu.");
        }
        if (post.getStatus() == RequestStatus.FULFILLED || post.getStatus() == RequestStatus.HIDDEN || post.getStatus() == RequestStatus.DELETED) {
            return OperationResult.fail("Bài hiện tại không còn hợp lệ để sửa.");
        }
        if (InputValidator.isBlank(title) || InputValidator.isBlank(content)) {
            return OperationResult.fail("Title và content không được trống.");
        }
        post.setTitle(title.trim());
        post.setContent(content.trim());
        if (post.getStatus() == RequestStatus.OPEN) {
            post.setStatus(RequestStatus.PENDING_REVIEW);
        }
        requestPostRepository.save(post);
        return OperationResult.ok("Sửa bài yêu cầu thành công.", post);
    }



    public OperationResult<RequestPost> deleteOwnPost(String postId) {
        UserAccount user = requireUser();
        RequestPost post = requestPostRepository.findById(postId).orElse(null);
        if (user == null || post == null || !post.getCreatorId().equals(user.getId())) {
            return OperationResult.fail("Chỉ chủ bài được xóa bài yêu cầu.");
        }
        if (post.getStatus() == RequestStatus.FULFILLED) {
            return OperationResult.fail("Bài FULFILLED không được xóa trong mô phỏng này.");
        }
        post.setDeleted(true);
        post.setStatus(RequestStatus.DELETED);
        requestPostRepository.save(post);
        return OperationResult.ok("Xóa bài yêu cầu thành công.", post);
    }



    public OperationResult<Comment> commentOpenPost(String postId, String content) {
        UserAccount user = requireUser();
        if (user == null) {
            return OperationResult.fail("Chỉ User đã đăng nhập được bình luận bài yêu cầu.");
        }
        RequestPost post = requestPostRepository.findById(postId).orElse(null);
        if (post == null || post.getStatus() != RequestStatus.OPEN || post.isHidden() || post.isDeleted()) {
            return OperationResult.fail("Chỉ được bình luận vào bài OPEN đang hiển thị.");
        }
        if (InputValidator.isBlank(content)) {
            return OperationResult.fail("Nội dung bình luận không được trống.");
        }
        Comment comment = new Comment(IdGenerator.nextId("COM"), user.getId(), "REQUEST", post.getId(), content.trim());
        commentRepository.save(comment);
        return OperationResult.ok("Bình luận vào bài yêu cầu thành công.", comment);
    }



    public OperationResult<RequestPost> markFulfilled(String postId, String documentId) {
        UserAccount user = requireUser();
        RequestPost post = requestPostRepository.findById(postId).orElse(null);
        if (user == null || post == null || !post.getCreatorId().equals(user.getId())) {
            return OperationResult.fail("Chỉ chủ bài được đánh dấu FULFILLED.");
        }
        if (post.getStatus() != RequestStatus.OPEN) {
            return OperationResult.fail("Chỉ bài OPEN mới được đánh dấu FULFILLED.");
        }
        if (!InputValidator.isBlank(documentId)) {
            DocumentItem document = documentRepository.findById(documentId).orElse(null);
            if (document == null || document.getStatus() != DocumentStatus.APPROVED) {
                return OperationResult.fail("documentId liên kết phải là tài liệu APPROVED.");
            }
            post.setLinkedDocumentId(documentId.trim());
        }
        post.setStatus(RequestStatus.FULFILLED);
        requestPostRepository.save(post);
        return OperationResult.ok("Đánh dấu bài yêu cầu FULFILLED thành công.", post);
    }



    public OperationResult<RequestPost> approvePost(String postId) {
        UserAccount admin = requireAdmin();
        if (admin == null) {
            return OperationResult.fail("Chỉ Admin được duyệt bài yêu cầu.");
        }
        RequestPost post = requestPostRepository.findById(postId).orElse(null);
        if (post == null || post.getStatus() != RequestStatus.PENDING_REVIEW) {
            return OperationResult.fail("Bài không tồn tại hoặc không ở trạng thái PENDING_REVIEW.");
        }
        post.setStatus(RequestStatus.OPEN);
        requestPostRepository.save(post);
        activityLogService.log(admin.getId(), "APPROVE_REQUEST", "REQUEST", post.getId(), "Admin duyệt bài yêu cầu tài liệu.");
        return OperationResult.ok("Duyệt bài yêu cầu thành công.", post);
    }



    public OperationResult<RequestPost> rejectPost(String postId, String reason) {
        UserAccount admin = requireAdmin();
        if (admin == null) {
            return OperationResult.fail("Chỉ Admin được từ chối bài yêu cầu.");
        }
        if (InputValidator.isBlank(reason)) {
            return OperationResult.fail("Lý do từ chối là bắt buộc.");
        }
        RequestPost post = requestPostRepository.findById(postId).orElse(null);
        if (post == null || post.getStatus() != RequestStatus.PENDING_REVIEW) {
            return OperationResult.fail("Bài không tồn tại hoặc không ở trạng thái PENDING_REVIEW.");
        }
        post.setStatus(RequestStatus.REJECTED);
        post.setRejectionReason(reason.trim());
        requestPostRepository.save(post);
        activityLogService.log(admin.getId(), "REJECT_REQUEST", "REQUEST", post.getId(), reason.trim());
        return OperationResult.ok("Từ chối bài yêu cầu thành công.", post);
    }



    public List<RequestPost> publicPosts() {
        return requestPostRepository.findPublicPosts();
    }



    public OperationResult<List<RequestPost>> pendingPosts() {
        if (requireAdmin() == null) {
            return OperationResult.fail("Chỉ Admin được xem bài yêu cầu chờ duyệt.");
        }
        return OperationResult.ok("Danh sách bài yêu cầu PENDING_REVIEW.", requestPostRepository.findByStatus(RequestStatus.PENDING_REVIEW));
    }

    private UserAccount requireUser() {
        UserAccount actor = sessionManager.getCurrentUser().orElse(null);
        return actor != null && actor.getRole() == Role.USER ? actor : null;
    }

    private UserAccount requireAdmin() {
        UserAccount actor = sessionManager.getCurrentUser().orElse(null);
        return actor != null && actor.getRole() == Role.ADMIN ? actor : null;
    }
}
