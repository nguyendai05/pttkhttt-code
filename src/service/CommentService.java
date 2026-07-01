package service;

import model.ActivityLog;
import model.Comment;
import model.DocumentItem;
import model.UserAccount;
import model.enums.DocumentStatus;
import model.enums.Role;
import repository.ActivityLogRepository;
import repository.CommentRepository;
import repository.DocumentRepository;
import util.IdGenerator;
import util.InputValidator;
import util.OperationResult;

import java.util.List;

/**
 * OWNER: Hồ Nguyễn Quốc Nam
 * FEATURE GROUP: Bình luận tài liệu
 * RELATED USE CASES: UC-7
 * PURPOSE: Quản lý comment tài liệu, sửa/xóa comment của chính mình và kiểm duyệt comment vi phạm.
 */
public class CommentService {
    private final CommentRepository commentRepository;
    private final DocumentRepository documentRepository;
    private final ActivityLogRepository activityLogRepository;
    private final SessionManager sessionManager;

    public CommentService(CommentRepository commentRepository, DocumentRepository documentRepository,
                          ActivityLogRepository activityLogRepository, SessionManager sessionManager) {
        this.commentRepository = commentRepository;
        this.documentRepository = documentRepository;
        this.activityLogRepository = activityLogRepository;
        this.sessionManager = sessionManager;
    }

    /**
     * OWNER: Hồ Nguyễn Quốc Nam
     * USE CASE: UC-7 - Bình luận tài liệu
     * ACTOR: User
     * FLOW: Basic Flow / Exception Flow
     * PURPOSE: User bình luận vào tài liệu APPROVED, nội dung không được trống.
     * SEQUENCE NOTE: ConsoleView -> DocumentInteractionController -> CommentService -> CommentRepository/DocumentRepository -> SessionManager.
     */
    public OperationResult<Comment> addComment(String documentId, String content) {
        UserAccount user = sessionManager.getCurrentUser().orElse(null);
        if (user == null || user.role != Role.USER) {
            return OperationResult.fail("Chỉ User đã đăng nhập được bình luận tài liệu.");
        }
        DocumentItem document = documentRepository.findById(documentId).orElse(null);
        if (document == null || document.status != DocumentStatus.APPROVED) {
            return OperationResult.fail("Chỉ được bình luận vào tài liệu APPROVED.");
        }
        if (InputValidator.isBlank(content)) {
            return OperationResult.fail("Nội dung bình luận không được trống.");
        }
        Comment comment = new Comment(IdGenerator.nextId("COM"), user.id, "DOCUMENT", documentId, content.trim());
        commentRepository.save(comment);
        return OperationResult.ok("Bình luận tài liệu thành công.", comment);
    }

    /**
     * OWNER: Hồ Nguyễn Quốc Nam
     * USE CASE: UC-7 - Xem bình luận tài liệu
     * ACTOR: Guest/User/Moderator/Admin
     * FLOW: Basic Flow
     * PURPOSE: Lấy danh sách comment chưa deleted của một tài liệu.
     * SEQUENCE NOTE: ConsoleView -> DocumentInteractionController -> CommentService -> CommentRepository/DocumentRepository -> SessionManager.
     */
    public OperationResult<List<Comment>> getDocumentComments(String documentId) {
        return OperationResult.ok("Danh sách bình luận tài liệu.", commentRepository.findByTarget("DOCUMENT", documentId));
    }

    /**
     * OWNER: Hồ Nguyễn Quốc Nam
     * USE CASE: UC-7 - Sửa bình luận của chính mình
     * ACTOR: User
     * FLOW: Alternative Flow / Exception Flow
     * PURPOSE: User chỉ được sửa comment do chính mình tạo.
     * SEQUENCE NOTE: ConsoleView -> DocumentInteractionController -> CommentService -> CommentRepository/DocumentRepository -> SessionManager.
     */
    public OperationResult<Comment> updateOwnComment(String commentId, String newContent) {
        UserAccount user = sessionManager.getCurrentUser().orElse(null);
        Comment comment = commentRepository.findById(commentId).orElse(null);
        if (user == null || comment == null || !comment.userId.equals(user.id)) {
            return OperationResult.fail("Chỉ chủ bình luận được sửa bình luận này.");
        }
        if (InputValidator.isBlank(newContent)) {
            return OperationResult.fail("Nội dung mới không được trống.");
        }
        comment.content = newContent.trim();
        commentRepository.save(comment);
        return OperationResult.ok("Sửa bình luận thành công.", comment);
    }

    /**
     * OWNER: Hồ Nguyễn Quốc Nam
     * USE CASE: UC-7 - Xóa bình luận của chính mình
     * ACTOR: User
     * FLOW: Alternative Flow / Exception Flow
     * PURPOSE: User chỉ được đánh dấu deleted comment do chính mình tạo.
     * SEQUENCE NOTE: ConsoleView -> DocumentInteractionController -> CommentService -> CommentRepository/DocumentRepository -> SessionManager.
     */
    public OperationResult<Comment> deleteOwnComment(String commentId) {
        UserAccount user = sessionManager.getCurrentUser().orElse(null);
        Comment comment = commentRepository.findById(commentId).orElse(null);
        if (user == null || comment == null || !comment.userId.equals(user.id)) {
            return OperationResult.fail("Chỉ chủ bình luận được xóa bình luận này.");
        }
        comment.deleted = true;
        commentRepository.save(comment);
        return OperationResult.ok("Xóa bình luận thành công.", comment);
    }

    /**
     * OWNER: Hồ Nguyễn Quốc Nam
     * USE CASE: UC-7 - Ẩn/xóa bình luận vi phạm
     * ACTOR: Moderator/Admin
     * FLOW: Alternative Flow / Exception Flow
     * PURPOSE: Moderator/Admin ẩn hoặc xóa comment vi phạm, bắt buộc lý do và ghi ActivityLog.
     * SEQUENCE NOTE: ConsoleView -> DocumentInteractionController -> CommentService -> CommentRepository/ActivityLogRepository -> SessionManager.
     */
    public OperationResult<Comment> moderateComment(String commentId, boolean delete, String reason) {
        UserAccount actor = sessionManager.getCurrentUser().orElse(null);
        if (actor == null || (actor.role != Role.MODERATOR && actor.role != Role.ADMIN)) {
            return OperationResult.fail("Chỉ Moderator/Admin được ẩn hoặc xóa bình luận vi phạm.");
        }
        if (InputValidator.isBlank(reason)) {
            return OperationResult.fail("Lý do xử lý là bắt buộc.");
        }
        Comment comment = commentRepository.findById(commentId).orElse(null);
        if (comment == null) {
            return OperationResult.fail("Không tìm thấy bình luận cần xử lý.");
        }
        comment.moderationReason = reason.trim();
        if (delete) {
            comment.deleted = true;
        } else {
            comment.hidden = true;
        }
        commentRepository.save(comment);
        activityLogRepository.save(new ActivityLog(IdGenerator.nextId("LOG"), actor.id,
                delete ? "DELETE_COMMENT" : "HIDE_COMMENT", "COMMENT", comment.id, reason.trim()));
        return OperationResult.ok((delete ? "Xóa" : "Ẩn") + " bình luận vi phạm thành công.", comment);
    }
}
