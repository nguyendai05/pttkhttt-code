package service;

import model.ActivityLog;
import model.DocumentItem;
import model.UserAccount;
import model.enums.DocumentStatus;
import model.enums.Role;
import repository.ActivityLogRepository;
import repository.DocumentRepository;
import util.IdGenerator;
import util.InputValidator;
import util.OperationResult;

import java.time.LocalDateTime;
import java.util.List;

/**
 * OWNER: Nguyễn Xuân Đại
 * FEATURE GROUP: Kiểm duyệt tài liệu
 * RELATED USE CASES: UC-5b
 * PURPOSE: Xử lý danh sách tài liệu chờ duyệt, approve/reject và ghi ActivityLog.
 */
public class DocumentReviewService {
    private final DocumentRepository documentRepository;
    private final ActivityLogRepository activityLogRepository;
    private final SessionManager sessionManager;

    public DocumentReviewService(DocumentRepository documentRepository, ActivityLogRepository activityLogRepository,
                                 SessionManager sessionManager) {
        this.documentRepository = documentRepository;
        this.activityLogRepository = activityLogRepository;
        this.sessionManager = sessionManager;
    }

    /**
     * OWNER: Nguyễn Xuân Đại
     * USE CASE: UC-5b - Xem tài liệu chờ duyệt
     * ACTOR: Moderator
     * FLOW: Basic Flow / Alternative Flow
     * PURPOSE: Moderator xem danh sách tài liệu đang ở trạng thái PENDING_REVIEW.
     * SEQUENCE NOTE: ConsoleView -> DocumentReviewController -> DocumentReviewService -> DocumentRepository -> SessionManager.
     */
    public OperationResult<List<DocumentItem>> getPendingDocuments() {
        UserAccount moderator = sessionManager.getCurrentUser().orElse(null);
        if (moderator == null || moderator.role != Role.MODERATOR) {
            return OperationResult.fail("Chỉ Moderator được xem tài liệu chờ duyệt.");
        }
        return OperationResult.ok("Danh sách tài liệu PENDING_REVIEW.", documentRepository.findByStatus(DocumentStatus.PENDING_REVIEW));
    }

    /**
     * OWNER: Nguyễn Xuân Đại
     * USE CASE: UC-5b - Phê duyệt tài liệu
     * ACTOR: Moderator
     * FLOW: Basic Flow
     * PURPOSE: Chuyển tài liệu PENDING_REVIEW sang APPROVED, ghi approvedBy, approvedAt và ActivityLog.
     * SEQUENCE NOTE: ConsoleView -> DocumentReviewController -> DocumentReviewService -> DocumentRepository/ActivityLogRepository -> SessionManager.
     */
    public OperationResult<DocumentItem> approve(String documentId) {
        UserAccount moderator = sessionManager.getCurrentUser().orElse(null);
        if (moderator == null || moderator.role != Role.MODERATOR) {
            return OperationResult.fail("Chỉ Moderator được phê duyệt tài liệu.");
        }
        DocumentItem document = documentRepository.findById(documentId).orElse(null);
        if (document == null || document.status != DocumentStatus.PENDING_REVIEW) {
            return OperationResult.fail("Tài liệu không tồn tại hoặc không ở trạng thái PENDING_REVIEW.");
        }
        document.status = DocumentStatus.APPROVED;
        document.approvedBy = moderator.id;
        document.approvedAt = LocalDateTime.now();
        document.rejectionReason = null;
        documentRepository.save(document);
        activityLogRepository.save(new ActivityLog(IdGenerator.nextId("LOG"), moderator.id, "APPROVE_DOCUMENT",
                "DOCUMENT", document.id, "Moderator phê duyệt tài liệu."));
        return OperationResult.ok("Phê duyệt tài liệu thành công.", document);
    }

    /**
     * OWNER: Nguyễn Xuân Đại
     * USE CASE: UC-5b - Từ chối tài liệu
     * ACTOR: Moderator
     * FLOW: Alternative Flow / Exception Flow
     * PURPOSE: Chuyển tài liệu PENDING_REVIEW sang REJECTED, bắt buộc nhập lý do và ghi ActivityLog.
     * SEQUENCE NOTE: ConsoleView -> DocumentReviewController -> DocumentReviewService -> DocumentRepository/ActivityLogRepository -> SessionManager.
     */
    public OperationResult<DocumentItem> reject(String documentId, String reason) {
        UserAccount moderator = sessionManager.getCurrentUser().orElse(null);
        if (moderator == null || moderator.role != Role.MODERATOR) {
            return OperationResult.fail("Chỉ Moderator được từ chối tài liệu.");
        }
        if (InputValidator.isBlank(reason)) {
            return OperationResult.fail("Lý do từ chối là bắt buộc.");
        }
        DocumentItem document = documentRepository.findById(documentId).orElse(null);
        if (document == null || document.status != DocumentStatus.PENDING_REVIEW) {
            return OperationResult.fail("Tài liệu không tồn tại hoặc không ở trạng thái PENDING_REVIEW.");
        }
        document.status = DocumentStatus.REJECTED;
        document.rejectionReason = reason.trim();
        documentRepository.save(document);
        activityLogRepository.save(new ActivityLog(IdGenerator.nextId("LOG"), moderator.id, "REJECT_DOCUMENT",
                "DOCUMENT", document.id, reason.trim()));
        return OperationResult.ok("Từ chối tài liệu thành công.", document);
    }
}
