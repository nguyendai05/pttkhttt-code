package controller;

import model.Comment;
import service.CommentService;
import service.RatingService;
import util.OperationResult;

import java.util.List;

/**
 * OWNER: Hồ Nguyễn Quốc Nam
 * FEATURE GROUP: Bình luận và đánh giá tài liệu
 * RELATED USE CASES: UC-7
 * PURPOSE: Điều phối comment, rating, sửa/xóa comment và kiểm duyệt comment.
 */
public class DocumentInteractionController {
    private CommentService commentService;
    private RatingService ratingService;

    public DocumentInteractionController(CommentService commentService, RatingService ratingService) {
        this.commentService = commentService;
        this.ratingService = ratingService;
    }

    /**
     * OWNER: Hồ Nguyễn Quốc Nam
     * USE CASE: UC-7 - Bình luận tài liệu
     * ACTOR: User
     * FLOW: Basic Flow / Exception Flow
     * PURPOSE: Điều phối thêm comment vào tài liệu APPROVED.
     * SEQUENCE NOTE: ConsoleView -> DocumentInteractionController -> CommentService -> CommentRepository/DocumentRepository -> SessionManager.
     */
    public OperationResult<Comment> addComment(String documentId, String content) {
        return commentService.addComment(documentId, content);
    }

    /**
     * OWNER: Hồ Nguyễn Quốc Nam
     * USE CASE: UC-7 - Đánh giá tài liệu
     * ACTOR: User
     * FLOW: Basic Flow / Alternative Flow / Exception Flow
     * PURPOSE: Điều phối rating 1-5 và cập nhật rating cũ nếu đã tồn tại.
     * SEQUENCE NOTE: ConsoleView -> DocumentInteractionController -> RatingService -> RatingRepository/DocumentRepository -> SessionManager.
     */
    public OperationResult<Double> rateDocument(String documentId, int score) {
        return ratingService.rateDocument(documentId, score);
    }

    /**
     * OWNER: Hồ Nguyễn Quốc Nam
     * USE CASE: UC-7 - Xem bình luận tài liệu
     * ACTOR: Guest/User/Moderator/Admin
     * FLOW: Basic Flow
     * PURPOSE: Điều phối lấy danh sách comment của tài liệu.
     * SEQUENCE NOTE: ConsoleView -> DocumentInteractionController -> CommentService -> CommentRepository/DocumentRepository -> SessionManager.
     */
    public OperationResult<List<Comment>> getDocumentComments(String documentId) {
        return commentService.getDocumentComments(documentId);
    }

    /**
     * OWNER: Hồ Nguyễn Quốc Nam
     * USE CASE: UC-7 - Sửa bình luận của chính mình
     * ACTOR: User
     * FLOW: Alternative Flow / Exception Flow
     * PURPOSE: Điều phối sửa comment của chính user đang đăng nhập.
     * SEQUENCE NOTE: ConsoleView -> DocumentInteractionController -> CommentService -> CommentRepository/DocumentRepository -> SessionManager.
     */
    public OperationResult<Comment> updateOwnComment(String commentId, String newContent) {
        return commentService.updateOwnComment(commentId, newContent);
    }

    /**
     * OWNER: Hồ Nguyễn Quốc Nam
     * USE CASE: UC-7 - Xóa bình luận của chính mình
     * ACTOR: User
     * FLOW: Alternative Flow / Exception Flow
     * PURPOSE: Điều phối xóa mềm comment của chính user đang đăng nhập.
     * SEQUENCE NOTE: ConsoleView -> DocumentInteractionController -> CommentService -> CommentRepository/DocumentRepository -> SessionManager.
     */
    public OperationResult<Comment> deleteOwnComment(String commentId) {
        return commentService.deleteOwnComment(commentId);
    }

    /**
     * OWNER: Hồ Nguyễn Quốc Nam
     * USE CASE: UC-7 - Ẩn/xóa bình luận vi phạm
     * ACTOR: Moderator/Admin
     * FLOW: Alternative Flow / Exception Flow
     * PURPOSE: Điều phối kiểm duyệt comment vi phạm, bắt buộc nhập lý do.
     * SEQUENCE NOTE: ConsoleView -> DocumentInteractionController -> CommentService -> CommentRepository/ActivityLogRepository -> SessionManager.
     */
    public OperationResult<Comment> moderateComment(String commentId, boolean delete, String reason) {
        return commentService.moderateComment(commentId, delete, reason);
    }
}
