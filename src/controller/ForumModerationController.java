package controller;

import model.Comment;
import model.RequestPost;
import service.ForumModerationService;
import util.OperationResult;


public class ForumModerationController {
    private ForumModerationService forumModerationService;

    public ForumModerationController(ForumModerationService forumModerationService) {
        this.forumModerationService = forumModerationService;
    }



    public OperationResult<String> viewForumContent() {
        return forumModerationService.viewForumContent();
    }



    public OperationResult<RequestPost> moderatePost(String postId, boolean delete, String reason) {
        return forumModerationService.moderatePost(postId, delete, reason);
    }



    public OperationResult<Comment> moderateComment(String commentId, boolean delete, String reason) {
        return forumModerationService.moderateComment(commentId, delete, reason);
    }
}
