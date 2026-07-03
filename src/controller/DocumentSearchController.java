package controller;

import model.DocumentItem;
import service.DocumentSearchService;
import util.OperationResult;

import java.util.List;

/**
 * OWNER: Hồ Nguyễn Quốc Nam
 * FEATURE GROUP: Tra cứu và khai thác tài liệu
 * RELATED USE CASES: UC-6
 * PURPOSE: Điều phối tìm kiếm, xem chi tiết và tải xuống mô phỏng.
 */
public class DocumentSearchController {
    private DocumentSearchService documentSearchService;

    public DocumentSearchController(DocumentSearchService documentSearchService) {
        this.documentSearchService = documentSearchService;
    }

    /**
     * OWNER: Hồ Nguyễn Quốc Nam
     * USE CASE: UC-6 - Tra cứu tài liệu
     * ACTOR: Guest/User/Moderator/Admin
     * FLOW: Basic Flow
     * PURPOSE: Tìm tài liệu APPROVED theo keyword từ View.
     * SEQUENCE NOTE: ConsoleView -> DocumentSearchController -> DocumentSearchService -> DocumentRepository -> SessionManager.
     */
    public OperationResult<List<DocumentItem>> searchApproved(String keyword) {
        return documentSearchService.searchApproved(keyword);
    }

    /**
     * OWNER: Hồ Nguyễn Quốc Nam
     * USE CASE: UC-6 - Xem chi tiết tài liệu
     * ACTOR: User/Moderator/Admin
     * FLOW: Basic Flow / Exception Flow
     * PURPOSE: Gọi service xem chi tiết tài liệu APPROVED.
     * SEQUENCE NOTE: ConsoleView -> DocumentSearchController -> DocumentSearchService -> DocumentRepository -> SessionManager.
     */
    public OperationResult<DocumentItem> viewDetail(String documentId) {
        return documentSearchService.viewDetail(documentId);
    }

    /**
     * OWNER: Hồ Nguyễn Quốc Nam
     * USE CASE: UC-6 - Tải xuống mô phỏng
     * ACTOR: User/Moderator/Admin
     * FLOW: Basic Flow / Exception Flow
     * PURPOSE: Gọi service tăng downloadCount cho tài liệu APPROVED.
     * SEQUENCE NOTE: ConsoleView -> DocumentSearchController -> DocumentSearchService -> DocumentRepository -> SessionManager.
     */
    public OperationResult<DocumentItem> download(String documentId) {
        return documentSearchService.download(documentId);
    }
}
