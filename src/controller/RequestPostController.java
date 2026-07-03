package controller;

import model.Comment;
import model.RequestPost;
import service.RequestPostService;
import util.OperationResult;

import java.util.List;

/**
 * OWNER: Tạ Văn Huy
 * FEATURE GROUP: Forum yêu cầu tài liệu
 * RELATED USE CASES: UC-10a, UC-10b
 * PURPOSE: Nhận request tạo/quản lý/duyệt bài yêu cầu từ ConsoleView và gọi RequestPostService.
 */
public class RequestPostController {
    private RequestPostService requestPostService;

    public RequestPostController(RequestPostService requestPostService) {
        this.requestPostService = requestPostService;
    }

    /**
     * OWNER: Tạ Văn Huy
     * USE CASE: UC-10a - Tạo bài yêu cầu tài liệu
     * ACTOR: User
     * FLOW: Basic Flow / Exception Flow
     * PURPOSE: Điều phối tạo RequestPost PENDING_REVIEW.
     * SEQUENCE NOTE: ConsoleView -> RequestPostController -> RequestPostService -> RequestPostRepository -> SessionManager.
     */
    public OperationResult<RequestPost> createPost(String title, String content) {
        return requestPostService.createPost(title, content);
    }

    /**
     * OWNER: Tạ Văn Huy
     * USE CASE: UC-10a - Xem bài yêu cầu của tôi
     * ACTOR: User
     * FLOW: Basic Flow
     * PURPOSE: Điều phối lấy danh sách bài yêu cầu của user đang đăng nhập.
     * SEQUENCE NOTE: ConsoleView -> RequestPostController -> RequestPostService -> RequestPostRepository -> SessionManager.
     */
    public OperationResult<List<RequestPost>> myPosts() {
        return requestPostService.myPosts();
    }

    /**
     * OWNER: Tạ Văn Huy
     * USE CASE: UC-10a - Sửa bài yêu cầu của tôi
     * ACTOR: User
     * FLOW: Alternative Flow / Exception Flow
     * PURPOSE: Điều phối sửa bài yêu cầu của chính user.
     * SEQUENCE NOTE: ConsoleView -> RequestPostController -> RequestPostService -> RequestPostRepository -> SessionManager.
     */
    public OperationResult<RequestPost> updateOwnPost(String postId, String title, String content) {
        return requestPostService.updateOwnPost(postId, title, content);
    }

    /**
     * OWNER: Tạ Văn Huy
     * USE CASE: UC-10a - Xóa bài yêu cầu của tôi
     * ACTOR: User
     * FLOW: Alternative Flow / Exception Flow
     * PURPOSE: Điều phối xóa mềm bài yêu cầu của chính user.
     * SEQUENCE NOTE: ConsoleView -> RequestPostController -> RequestPostService -> RequestPostRepository -> SessionManager.
     */
    public OperationResult<RequestPost> deleteOwnPost(String postId) {
        return requestPostService.deleteOwnPost(postId);
    }

    /**
     * OWNER: Tạ Văn Huy
     * USE CASE: UC-10a - Bình luận vào bài OPEN
     * ACTOR: User
     * FLOW: Alternative Flow / Exception Flow
     * PURPOSE: Điều phối bình luận một cấp vào bài yêu cầu OPEN.
     * SEQUENCE NOTE: ConsoleView -> RequestPostController -> RequestPostService -> CommentRepository/RequestPostRepository -> SessionManager.
     */
    public OperationResult<Comment> commentOpenPost(String postId, String content) {
        return requestPostService.commentOpenPost(postId, content);
    }

    /**
     * OWNER: Tạ Văn Huy
     * USE CASE: UC-10a - Đánh dấu yêu cầu đã đáp ứng
     * ACTOR: User
     * FLOW: Alternative Flow / Exception Flow
     * PURPOSE: Điều phối chủ bài chuyển bài OPEN sang FULFILLED và liên kết tài liệu nếu có.
     * SEQUENCE NOTE: ConsoleView -> RequestPostController -> RequestPostService -> RequestPostRepository/DocumentRepository -> SessionManager.
     */
    public OperationResult<RequestPost> markFulfilled(String postId, String documentId) {
        return requestPostService.markFulfilled(postId, documentId);
    }

    /**
     * OWNER: Tạ Văn Huy
     * USE CASE: UC-10b - Duyệt bài yêu cầu tài liệu
     * ACTOR: Admin
     * FLOW: Basic Flow
     * PURPOSE: Điều phối Admin approve bài PENDING_REVIEW sang OPEN.
     * SEQUENCE NOTE: ConsoleView -> RequestPostController -> RequestPostService -> RequestPostRepository/ActivityLogRepository -> SessionManager.
     */
    public OperationResult<RequestPost> approvePost(String postId) {
        return requestPostService.approvePost(postId);
    }

    /**
     * OWNER: Tạ Văn Huy
     * USE CASE: UC-10b - Từ chối bài yêu cầu tài liệu
     * ACTOR: Admin
     * FLOW: Alternative Flow / Exception Flow
     * PURPOSE: Điều phối Admin reject bài PENDING_REVIEW kèm lý do.
     * SEQUENCE NOTE: ConsoleView -> RequestPostController -> RequestPostService -> RequestPostRepository/ActivityLogRepository -> SessionManager.
     */
    public OperationResult<RequestPost> rejectPost(String postId, String reason) {
        return requestPostService.rejectPost(postId, reason);
    }

    /**
     * OWNER: Tạ Văn Huy
     * USE CASE: UC-10b - Hiển thị bài yêu cầu công khai
     * ACTOR: User/Guest/Admin/Moderator
     * FLOW: Basic Flow
     * PURPOSE: Điều phối lấy các bài OPEN/FULFILLED công khai.
     * SEQUENCE NOTE: ConsoleView -> RequestPostController -> RequestPostService -> RequestPostRepository -> SessionManager.
     */
    public List<RequestPost> publicPosts() {
        return requestPostService.publicPosts();
    }

    /**
     * OWNER: Tạ Văn Huy
     * USE CASE: UC-10b - Xem bài yêu cầu chờ duyệt
     * ACTOR: Admin
     * FLOW: Basic Flow / Exception Flow
     * PURPOSE: Điều phối lấy danh sách bài PENDING_REVIEW cho Admin.
     * SEQUENCE NOTE: ConsoleView -> RequestPostController -> RequestPostService -> RequestPostRepository -> SessionManager.
     */
    public OperationResult<List<RequestPost>> pendingPosts() {
        return requestPostService.pendingPosts();
    }
}
