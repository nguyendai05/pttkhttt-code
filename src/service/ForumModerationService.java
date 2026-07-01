package service;

import model.ActivityLog;
import model.Comment;
import model.RequestPost;
import model.UserAccount;
import model.enums.RequestStatus;
import model.enums.Role;
import repository.ActivityLogRepository;
import repository.CommentRepository;
import repository.RequestPostRepository;
import util.IdGenerator;
import util.InputValidator;
import util.OperationResult;

import java.util.List;

/**
 * OWNER: Tạ Văn Huy
 * FEATURE GROUP: Kiểm soát nội dung forum
 * RELATED USE CASES: UC-10c
 * PURPOSE: Moderator xem, ẩn hoặc xóa bài viết/bình luận vi phạm và ghi ActivityLog.
 */
public class ForumModerationService {
    private final RequestPostRepository requestPostRepository;
    private final CommentRepository commentRepository;
    private final ActivityLogRepository activityLogRepository;
    private final SessionManager sessionManager;

    public ForumModerationService(RequestPostRepository requestPostRepository, CommentRepository commentRepository,
                                  ActivityLogRepository activityLogRepository, SessionManager sessionManager) {
        this.requestPostRepository = requestPostRepository;
        this.commentRepository = commentRepository;
        this.activityLogRepository = activityLogRepository;
        this.sessionManager = sessionManager;
    }

    /**
     * OWNER: Tạ Văn Huy
     * USE CASE: UC-10c - Xem nội dung forum
     * ACTOR: Moderator
     * FLOW: Basic Flow / Alternative Flow
     * PURPOSE: Moderator xem bài công khai OPEN/FULFILLED và danh sách comment hiện có để xử lý vi phạm.
     * SEQUENCE NOTE: ConsoleView -> ForumModerationController -> ForumModerationService -> RequestPostRepository/CommentRepository -> SessionManager.
     */
    public OperationResult<String> viewForumContent() {
        UserAccount moderator = requireModerator();
        if (moderator == null) {
            return OperationResult.fail("Chỉ Moderator được xem màn hình kiểm soát forum.");
        }
        StringBuilder builder = new StringBuilder();
        builder.append("Public posts:").append(System.lineSeparator());
        for (RequestPost post : requestPostRepository.findPublicPosts()) {
            builder.append(" - ").append(post).append(System.lineSeparator());
        }
        builder.append("Comments:").append(System.lineSeparator());
        for (Comment comment : commentRepository.findAll()) {
            builder.append(" - ").append(comment).append(System.lineSeparator());
        }
        return OperationResult.ok("Danh sách nội dung forum.", builder.toString());
    }

    /**
     * OWNER: Tạ Văn Huy
     * USE CASE: UC-10c - Ẩn/xóa bài viết vi phạm
     * ACTOR: Moderator
     * FLOW: Basic Flow / Alternative Flow / Exception Flow
     * PURPOSE: Moderator ẩn hoặc xóa bài viết vi phạm, bắt buộc nhập lý do, in thông báo mô phỏng và ghi ActivityLog.
     * SEQUENCE NOTE: ConsoleView -> ForumModerationController -> ForumModerationService -> RequestPostRepository/ActivityLogRepository -> SessionManager.
     */
    public OperationResult<RequestPost> moderatePost(String postId, boolean delete, String reason) {
        UserAccount moderator = requireModerator();
        if (moderator == null) {
            return OperationResult.fail("Chỉ Moderator được kiểm soát bài viết forum.");
        }
        if (InputValidator.isBlank(reason)) {
            return OperationResult.fail("Lý do xử lý là bắt buộc.");
        }
        RequestPost post = requestPostRepository.findById(postId).orElse(null);
        if (post == null) {
            return OperationResult.fail("Không tìm thấy bài viết forum.");
        }
        if (delete) {
            post.deleted = true;
            post.status = RequestStatus.DELETED;
        } else {
            post.hidden = true;
            post.status = RequestStatus.HIDDEN;
        }
        requestPostRepository.save(post);
        activityLogRepository.save(new ActivityLog(IdGenerator.nextId("LOG"), moderator.id,
                delete ? "DELETE_FORUM_POST" : "HIDE_FORUM_POST", "REQUEST", post.id, reason.trim()));
        System.out.println("Thông báo mô phỏng tới người đăng " + post.creatorId + ": Nội dung bị xử lý. Lý do: " + reason.trim());
        return OperationResult.ok((delete ? "Xóa" : "Ẩn") + " bài viết forum thành công.", post);
    }

    /**
     * OWNER: Tạ Văn Huy
     * USE CASE: UC-10c - Ẩn/xóa bình luận vi phạm
     * ACTOR: Moderator
     * FLOW: Basic Flow / Alternative Flow / Exception Flow
     * PURPOSE: Moderator ẩn hoặc xóa bình luận vi phạm, bắt buộc nhập lý do, in thông báo mô phỏng và ghi ActivityLog.
     * SEQUENCE NOTE: ConsoleView -> ForumModerationController -> ForumModerationService -> CommentRepository/ActivityLogRepository -> SessionManager.
     */
    public OperationResult<Comment> moderateComment(String commentId, boolean delete, String reason) {
        UserAccount moderator = requireModerator();
        if (moderator == null) {
            return OperationResult.fail("Chỉ Moderator được kiểm soát bình luận forum.");
        }
        if (InputValidator.isBlank(reason)) {
            return OperationResult.fail("Lý do xử lý là bắt buộc.");
        }
        Comment comment = commentRepository.findById(commentId).orElse(null);
        if (comment == null) {
            return OperationResult.fail("Không tìm thấy bình luận.");
        }
        comment.moderationReason = reason.trim();
        if (delete) {
            comment.deleted = true;
        } else {
            comment.hidden = true;
        }
        commentRepository.save(comment);
        activityLogRepository.save(new ActivityLog(IdGenerator.nextId("LOG"), moderator.id,
                delete ? "DELETE_FORUM_COMMENT" : "HIDE_FORUM_COMMENT", "COMMENT", comment.id, reason.trim()));
        System.out.println("Thông báo mô phỏng tới người bình luận " + comment.userId + ": Bình luận bị xử lý. Lý do: " + reason.trim());
        return OperationResult.ok((delete ? "Xóa" : "Ẩn") + " bình luận forum thành công.", comment);
    }

    private UserAccount requireModerator() {
        UserAccount actor = sessionManager.getCurrentUser().orElse(null);
        return actor != null && actor.role == Role.MODERATOR ? actor : null;
    }
}
