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
 * OWNER: Táº¡ VÄƒn Huy
 * FEATURE GROUP: Kiá»ƒm soĂ¡t ná»™i dung forum
 * RELATED USE CASES: UC-10c
 * PURPOSE: Moderator xem, áº©n hoáº·c xĂ³a bĂ i viáº¿t/bĂ¬nh luáº­n vi pháº¡m vĂ  ghi ActivityLog.
 */
public class ForumModerationService {
    private RequestPostRepository requestPostRepository;
    private CommentRepository commentRepository;
    private ActivityLogRepository activityLogRepository;
    private SessionManager sessionManager;

    public ForumModerationService(RequestPostRepository requestPostRepository, CommentRepository commentRepository,
                                  ActivityLogRepository activityLogRepository, SessionManager sessionManager) {
        this.requestPostRepository = requestPostRepository;
        this.commentRepository = commentRepository;
        this.activityLogRepository = activityLogRepository;
        this.sessionManager = sessionManager;
    }

    /**
     * OWNER: Táº¡ VÄƒn Huy
     * USE CASE: UC-10c - Xem ná»™i dung forum
     * ACTOR: Moderator
     * FLOW: Basic Flow / Alternative Flow
     * PURPOSE: Moderator xem bĂ i cĂ´ng khai OPEN/FULFILLED vĂ  danh sĂ¡ch comment hiá»‡n cĂ³ Ä‘á»ƒ xá»­ lĂ½ vi pháº¡m.
     * SEQUENCE NOTE: ConsoleView -> ForumModerationController -> ForumModerationService -> RequestPostRepository/CommentRepository -> SessionManager.
     */
    public OperationResult<String> viewForumContent() {
        UserAccount moderator = requireModerator();
        if (moderator == null) {
            return OperationResult.fail("Chá»‰ Moderator Ä‘Æ°á»£c xem mĂ n hĂ¬nh kiá»ƒm soĂ¡t forum.");
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
        return OperationResult.ok("Danh sĂ¡ch ná»™i dung forum.", builder.toString());
    }

    /**
     * OWNER: Táº¡ VÄƒn Huy
     * USE CASE: UC-10c - áº¨n/xĂ³a bĂ i viáº¿t vi pháº¡m
     * ACTOR: Moderator
     * FLOW: Basic Flow / Alternative Flow / Exception Flow
     * PURPOSE: Moderator áº©n hoáº·c xĂ³a bĂ i viáº¿t vi pháº¡m, báº¯t buá»™c nháº­p lĂ½ do, in thĂ´ng bĂ¡o mĂ´ phá»ng vĂ  ghi ActivityLog.
     * SEQUENCE NOTE: ConsoleView -> ForumModerationController -> ForumModerationService -> RequestPostRepository/ActivityLogRepository -> SessionManager.
     */
    public OperationResult<RequestPost> moderatePost(String postId, boolean delete, String reason) {
        UserAccount moderator = requireModerator();
        if (moderator == null) {
            return OperationResult.fail("Chá»‰ Moderator Ä‘Æ°á»£c kiá»ƒm soĂ¡t bĂ i viáº¿t forum.");
        }
        if (InputValidator.isBlank(reason)) {
            return OperationResult.fail("LĂ½ do xá»­ lĂ½ lĂ  báº¯t buá»™c.");
        }
        RequestPost post = requestPostRepository.findById(postId).orElse(null);
        if (post == null) {
            return OperationResult.fail("KhĂ´ng tĂ¬m tháº¥y bĂ i viáº¿t forum.");
        }
        if (delete) {
            post.setDeleted(true);
            post.setStatus(RequestStatus.DELETED);
        } else {
            post.setHidden(true);
            post.setStatus(RequestStatus.HIDDEN);
        }
        requestPostRepository.save(post);
        activityLogRepository.save(new ActivityLog(IdGenerator.nextId("LOG"), moderator.getId(),
                delete ? "DELETE_FORUM_POST" : "HIDE_FORUM_POST", "REQUEST", post.getId(), reason.trim()));
        System.out.println("ThĂ´ng bĂ¡o mĂ´ phá»ng tá»›i ngÆ°á»i Ä‘Äƒng " + post.getCreatorId() + ": Ná»™i dung bá»‹ xá»­ lĂ½. LĂ½ do: " + reason.trim());
        return OperationResult.ok((delete ? "XĂ³a" : "áº¨n") + " bĂ i viáº¿t forum thĂ nh cĂ´ng.", post);
    }

    /**
     * OWNER: Táº¡ VÄƒn Huy
     * USE CASE: UC-10c - áº¨n/xĂ³a bĂ¬nh luáº­n vi pháº¡m
     * ACTOR: Moderator
     * FLOW: Basic Flow / Alternative Flow / Exception Flow
     * PURPOSE: Moderator áº©n hoáº·c xĂ³a bĂ¬nh luáº­n vi pháº¡m, báº¯t buá»™c nháº­p lĂ½ do, in thĂ´ng bĂ¡o mĂ´ phá»ng vĂ  ghi ActivityLog.
     * SEQUENCE NOTE: ConsoleView -> ForumModerationController -> ForumModerationService -> CommentRepository/ActivityLogRepository -> SessionManager.
     */
    public OperationResult<Comment> moderateComment(String commentId, boolean delete, String reason) {
        UserAccount moderator = requireModerator();
        if (moderator == null) {
            return OperationResult.fail("Chá»‰ Moderator Ä‘Æ°á»£c kiá»ƒm soĂ¡t bĂ¬nh luáº­n forum.");
        }
        if (InputValidator.isBlank(reason)) {
            return OperationResult.fail("LĂ½ do xá»­ lĂ½ lĂ  báº¯t buá»™c.");
        }
        Comment comment = commentRepository.findById(commentId).orElse(null);
        if (comment == null) {
            return OperationResult.fail("KhĂ´ng tĂ¬m tháº¥y bĂ¬nh luáº­n.");
        }
        comment.setModerationReason(reason.trim());
        if (delete) {
            comment.setDeleted(true);
        } else {
            comment.setHidden(true);
        }
        commentRepository.save(comment);
        activityLogRepository.save(new ActivityLog(IdGenerator.nextId("LOG"), moderator.getId(),
                delete ? "DELETE_FORUM_COMMENT" : "HIDE_FORUM_COMMENT", "COMMENT", comment.getId(), reason.trim()));
        System.out.println("ThĂ´ng bĂ¡o mĂ´ phá»ng tá»›i ngÆ°á»i bĂ¬nh luáº­n " + comment.getUserId() + ": BĂ¬nh luáº­n bá»‹ xá»­ lĂ½. LĂ½ do: " + reason.trim());
        return OperationResult.ok((delete ? "XĂ³a" : "áº¨n") + " bĂ¬nh luáº­n forum thĂ nh cĂ´ng.", comment);
    }

    private UserAccount requireModerator() {
        UserAccount actor = sessionManager.getCurrentUser().orElse(null);
        return actor != null && actor.getRole() == Role.MODERATOR ? actor : null;
    }
}
