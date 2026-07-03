package controller;

import model.Comment;
import service.CommentService;
import service.RatingService;
import util.OperationResult;

import java.util.List;


public class DocumentInteractionController {
    private CommentService commentService;
    private RatingService ratingService;

    public DocumentInteractionController(CommentService commentService, RatingService ratingService) {
        this.commentService = commentService;
        this.ratingService = ratingService;
    }



    public OperationResult<Comment> addComment(String documentId, String content) {
        return commentService.addComment(documentId, content);
    }



    public OperationResult<Double> rateDocument(String documentId, int score) {
        return ratingService.rateDocument(documentId, score);
    }



    public OperationResult<List<Comment>> getDocumentComments(String documentId) {
        return commentService.getDocumentComments(documentId);
    }



    public OperationResult<Comment> updateOwnComment(String commentId, String newContent) {
        return commentService.updateOwnComment(commentId, newContent);
    }



    public OperationResult<Comment> deleteOwnComment(String commentId) {
        return commentService.deleteOwnComment(commentId);
    }



    public OperationResult<Comment> moderateComment(String commentId, boolean delete, String reason) {
        return commentService.moderateComment(commentId, delete, reason);
    }
}
