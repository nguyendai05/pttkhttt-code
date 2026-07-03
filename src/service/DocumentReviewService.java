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


public class DocumentReviewService {
    private DocumentRepository documentRepository;
    private ActivityLogRepository activityLogRepository;
    private SessionManager sessionManager;

    public DocumentReviewService(DocumentRepository documentRepository, ActivityLogRepository activityLogRepository,
                                 SessionManager sessionManager) {
        this.documentRepository = documentRepository;
        this.activityLogRepository = activityLogRepository;
        this.sessionManager = sessionManager;
    }



    public OperationResult<List<DocumentItem>> getPendingDocuments() {
        UserAccount moderator = sessionManager.getCurrentUser().orElse(null);
        if (moderator == null || moderator.getRole() != Role.MODERATOR) {
            return OperationResult.fail("Chỉ Moderator được xem tài liệu chờ duyệt.");
        }
        return OperationResult.ok("Danh sách tài liệu PENDING_REVIEW.", documentRepository.findByStatus(DocumentStatus.PENDING_REVIEW));
    }



    public OperationResult<DocumentItem> approve(String documentId) {
        UserAccount moderator = sessionManager.getCurrentUser().orElse(null);
        if (moderator == null || moderator.getRole() != Role.MODERATOR) {
            return OperationResult.fail("Chỉ Moderator được phê duyệt tài liệu.");
        }
        DocumentItem document = documentRepository.findById(documentId).orElse(null);
        if (document == null || document.getStatus() != DocumentStatus.PENDING_REVIEW) {
            return OperationResult.fail("Tài liệu không tồn tại hoặc không ở trạng thái PENDING_REVIEW.");
        }
        document.setStatus(DocumentStatus.APPROVED);
        document.setApprovedBy(moderator.getId());
        document.setApprovedAt(LocalDateTime.now());
        document.setRejectionReason(null);
        documentRepository.save(document);
        activityLogRepository.save(new ActivityLog(IdGenerator.nextId("LOG"), moderator.getId(), "APPROVE_DOCUMENT",
                "DOCUMENT", document.getId(), "Moderator phê duyệt tài liệu."));
        return OperationResult.ok("Phê duyệt tài liệu thành công.", document);
    }



    public OperationResult<DocumentItem> reject(String documentId, String reason) {
        UserAccount moderator = sessionManager.getCurrentUser().orElse(null);
        if (moderator == null || moderator.getRole() != Role.MODERATOR) {
            return OperationResult.fail("Chỉ Moderator được từ chối tài liệu.");
        }
        if (InputValidator.isBlank(reason)) {
            return OperationResult.fail("Lý do từ chối là bắt buộc.");
        }
        DocumentItem document = documentRepository.findById(documentId).orElse(null);
        if (document == null || document.getStatus() != DocumentStatus.PENDING_REVIEW) {
            return OperationResult.fail("Tài liệu không tồn tại hoặc không ở trạng thái PENDING_REVIEW.");
        }
        document.setStatus(DocumentStatus.REJECTED);
        document.setRejectionReason(reason.trim());
        documentRepository.save(document);
        activityLogRepository.save(new ActivityLog(IdGenerator.nextId("LOG"), moderator.getId(), "REJECT_DOCUMENT",
                "DOCUMENT", document.getId(), reason.trim()));
        return OperationResult.ok("Từ chối tài liệu thành công.", document);
    }
}
