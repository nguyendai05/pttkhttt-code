package controller;

import model.Comment;
import model.RequestPost;
import service.ForumModerationService;
import util.OperationResult;

/**
 * OWNER: Tạ Văn Huy
 * FEATURE GROUP: Kiểm soát nội dung forum
 * RELATED USE CASES: UC-10c
 * PURPOSE: Nhận request kiểm soát nội dung forum từ ConsoleView và gọi ForumModerationService.
 */
public class ForumModerationController {
    private ForumModerationService forumModerationService;

    public ForumModerationController(ForumModerationService forumModerationService) {
        this.forumModerationService = forumModerationService;
    }

    /**
     * OWNER: Tạ Văn Huy
     * USE CASE: UC-10c - Xem nội dung forum
     * ACTOR: Moderator
     * FLOW: Basic Flow / Alternative Flow
     * PURPOSE: Điều phối Moderator xem bài viết và bình luận trên forum.
     * SEQUENCE NOTE: ConsoleView -> ForumModerationController -> ForumModerationService -> RequestPostRepository/CommentRepository -> SessionManager.
     */
    public OperationResult<String> viewForumContent() {
        return forumModerationService.viewForumContent();
    }

    /**
     * OWNER: Tạ Văn Huy
     * USE CASE: UC-10c - Ẩn/xóa bài viết vi phạm
     * ACTOR: Moderator
     * FLOW: Basic Flow / Alternative Flow / Exception Flow
     * PURPOSE: Điều phối ẩn hoặc xóa bài viết vi phạm, bắt buộc lý do.
     * SEQUENCE NOTE: ConsoleView -> ForumModerationController -> ForumModerationService -> RequestPostRepository/ActivityLogRepository -> SessionManager.
     */
    public OperationResult<RequestPost> moderatePost(String postId, boolean delete, String reason) {
        return forumModerationService.moderatePost(postId, delete, reason);
    }

    /**
     * OWNER: Tạ Văn Huy
     * USE CASE: UC-10c - Ẩn/xóa bình luận vi phạm
     * ACTOR: Moderator
     * FLOW: Basic Flow / Alternative Flow / Exception Flow
     * PURPOSE: Điều phối ẩn hoặc xóa bình luận vi phạm, bắt buộc lý do.
     * SEQUENCE NOTE: ConsoleView -> ForumModerationController -> ForumModerationService -> CommentRepository/ActivityLogRepository -> SessionManager.
     */
    public OperationResult<Comment> moderateComment(String commentId, boolean delete, String reason) {
        return forumModerationService.moderateComment(commentId, delete, reason);
    }
}
