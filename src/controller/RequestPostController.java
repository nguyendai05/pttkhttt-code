package controller;

import model.Comment;
import model.RequestPost;
import service.RequestPostService;
import util.OperationResult;

import java.util.List;


public class RequestPostController {
    private RequestPostService requestPostService;

    public RequestPostController(RequestPostService requestPostService) {
        this.requestPostService = requestPostService;
    }



    public OperationResult<RequestPost> createPost(String title, String content) {
        return requestPostService.createPost(title, content);
    }



    public OperationResult<List<RequestPost>> myPosts() {
        return requestPostService.myPosts();
    }



    public OperationResult<RequestPost> updateOwnPost(String postId, String title, String content) {
        return requestPostService.updateOwnPost(postId, title, content);
    }



    public OperationResult<RequestPost> deleteOwnPost(String postId) {
        return requestPostService.deleteOwnPost(postId);
    }



    public OperationResult<Comment> commentOpenPost(String postId, String content) {
        return requestPostService.commentOpenPost(postId, content);
    }



    public OperationResult<RequestPost> markFulfilled(String postId, String documentId) {
        return requestPostService.markFulfilled(postId, documentId);
    }



    public OperationResult<RequestPost> approvePost(String postId) {
        return requestPostService.approvePost(postId);
    }



    public OperationResult<RequestPost> rejectPost(String postId, String reason) {
        return requestPostService.rejectPost(postId, reason);
    }



    public List<RequestPost> publicPosts() {
        return requestPostService.publicPosts();
    }



    public OperationResult<List<RequestPost>> pendingPosts() {
        return requestPostService.pendingPosts();
    }
}
