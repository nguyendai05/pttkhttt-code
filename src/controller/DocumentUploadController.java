package controller;

import model.DocumentItem;
import service.DocumentUploadService;
import util.OperationResult;

/**
 * OWNER: Nguyễn Xuân Đại
 * FEATURE GROUP: Upload tài liệu
 * RELATED USE CASES: UC-5a
 * PURPOSE: Nhận request upload giả lập từ ConsoleView và chuyển cho DocumentUploadService.
 */
public class DocumentUploadController {
    private DocumentUploadService documentUploadService;

    public DocumentUploadController(DocumentUploadService documentUploadService) {
        this.documentUploadService = documentUploadService;
    }

    /**
     * OWNER: Nguyễn Xuân Đại
     * USE CASE: UC-5a - Tải lên tài liệu
     * ACTOR: User
     * FLOW: Basic Flow / Exception Flow
     * PURPOSE: Điều phối metadata tài liệu từ View sang Service.
     * SEQUENCE NOTE: ConsoleView -> DocumentUploadController -> DocumentUploadService -> DocumentRepository -> SessionManager.
     */
    public OperationResult<DocumentItem> upload(String fileName, String title, String description, String category,
                                                String authorOrSource, String keywordsCsv, int year, double fileSizeMB) {
        return documentUploadService.upload(fileName, title, description, category, authorOrSource, keywordsCsv, year, fileSizeMB);
    }
}
