package controller;

import model.DocumentItem;
import service.DocumentReviewService;
import util.OperationResult;

import java.util.List;

/**
 * OWNER: Nguyễn Xuân Đại
 * FEATURE GROUP: Kiểm duyệt tài liệu
 * RELATED USE CASES: UC-5b
 * PURPOSE: Điều phối xem pending document, approve và reject.
 */
public class DocumentReviewController {
    private DocumentReviewService documentReviewService;

    public DocumentReviewController(DocumentReviewService documentReviewService) {
        this.documentReviewService = documentReviewService;
    }

    /**
     * OWNER: Nguyễn Xuân Đại
     * USE CASE: UC-5b - Xem tài liệu chờ duyệt
     * ACTOR: Moderator
     * FLOW: Basic Flow / Alternative Flow
     * PURPOSE: Lấy danh sách tài liệu PENDING_REVIEW cho Moderator.
     * SEQUENCE NOTE: ConsoleView -> DocumentReviewController -> DocumentReviewService -> DocumentRepository -> SessionManager.
     */
    public OperationResult<List<DocumentItem>> getPendingDocuments() {
        return documentReviewService.getPendingDocuments();
    }

    /**
     * OWNER: Nguyễn Xuân Đại
     * USE CASE: UC-5b - Phê duyệt tài liệu
     * ACTOR: Moderator
     * FLOW: Basic Flow
     * PURPOSE: Gọi service approve tài liệu chờ duyệt.
     * SEQUENCE NOTE: ConsoleView -> DocumentReviewController -> DocumentReviewService -> DocumentRepository/ActivityLogRepository -> SessionManager.
     */
    public OperationResult<DocumentItem> approve(String documentId) {
        return documentReviewService.approve(documentId);
    }

    /**
     * OWNER: Nguyễn Xuân Đại
     * USE CASE: UC-5b - Từ chối tài liệu
     * ACTOR: Moderator
     * FLOW: Alternative Flow / Exception Flow
     * PURPOSE: Gọi service reject tài liệu, bắt buộc lý do.
     * SEQUENCE NOTE: ConsoleView -> DocumentReviewController -> DocumentReviewService -> DocumentRepository/ActivityLogRepository -> SessionManager.
     */
    public OperationResult<DocumentItem> reject(String documentId, String reason) {
        return documentReviewService.reject(documentId, reason);
    }
}
