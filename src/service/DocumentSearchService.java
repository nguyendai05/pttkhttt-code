package service;

import model.DocumentItem;
import model.UserAccount;
import model.enums.DocumentStatus;
import repository.DocumentRepository;
import util.OperationResult;

import java.util.List;

/**
 * OWNER: Hồ Nguyễn Quốc Nam
 * FEATURE GROUP: Tra cứu và khai thác tài liệu
 * RELATED USE CASES: UC-6
 * PURPOSE: Tìm kiếm tài liệu APPROVED và mô phỏng tải xuống.
 */
public class DocumentSearchService {
    private DocumentRepository documentRepository;
    private SessionManager sessionManager;

    public DocumentSearchService(DocumentRepository documentRepository, SessionManager sessionManager) {
        this.documentRepository = documentRepository;
        this.sessionManager = sessionManager;
    }

    /**
     * OWNER: Hồ Nguyễn Quốc Nam
     * USE CASE: UC-6 - Tra cứu tài liệu
     * ACTOR: Guest/User/Moderator/Admin
     * FLOW: Basic Flow
     * PURPOSE: Tìm kiếm chỉ trong tài liệu APPROVED theo title, description, category, authorOrSource, keywords hoặc year.
     * SEQUENCE NOTE: ConsoleView -> DocumentSearchController -> DocumentSearchService -> DocumentRepository -> SessionManager.
     */
    public OperationResult<List<DocumentItem>> searchApproved(String keyword) {
        return OperationResult.ok("Kết quả tìm kiếm tài liệu APPROVED.", documentRepository.searchApproved(keyword));
    }

    /**
     * OWNER: Hồ Nguyễn Quốc Nam
     * USE CASE: UC-6 - Xem chi tiết tài liệu
     * ACTOR: User/Moderator/Admin
     * FLOW: Basic Flow / Exception Flow
     * PURPOSE: Người đăng nhập xem chi tiết tài liệu APPROVED; Guest chỉ nên dùng kết quả tìm kiếm cơ bản.
     * SEQUENCE NOTE: ConsoleView -> DocumentSearchController -> DocumentSearchService -> DocumentRepository -> SessionManager.
     */
    public OperationResult<DocumentItem> viewDetail(String documentId) {
        UserAccount currentUser = sessionManager.getCurrentUser().orElse(null);
        if (currentUser == null) {
            return OperationResult.fail("Guest chỉ được xem thông tin cơ bản từ kết quả tìm kiếm.");
        }
        DocumentItem document = documentRepository.findById(documentId).orElse(null);
        if (document == null || document.getStatus() != DocumentStatus.APPROVED) {
            return OperationResult.fail("Không tìm thấy tài liệu APPROVED.");
        }
        return OperationResult.ok("Chi tiết tài liệu APPROVED.", document);
    }

    /**
     * OWNER: Hồ Nguyễn Quốc Nam
     * USE CASE: UC-6 - Tải xuống mô phỏng
     * ACTOR: User/Moderator/Admin
     * FLOW: Basic Flow / Exception Flow
     * PURPOSE: Người đăng nhập tải xuống tài liệu APPROVED và tăng downloadCount, không tải file thật.
     * SEQUENCE NOTE: ConsoleView -> DocumentSearchController -> DocumentSearchService -> DocumentRepository -> SessionManager.
     */
    public OperationResult<DocumentItem> download(String documentId) {
        UserAccount currentUser = sessionManager.getCurrentUser().orElse(null);
        if (currentUser == null) {
            return OperationResult.fail("Guest không được tải xuống tài liệu.");
        }
        DocumentItem document = documentRepository.findById(documentId).orElse(null);
        if (document == null || document.getStatus() != DocumentStatus.APPROVED) {
            return OperationResult.fail("Không tìm thấy tài liệu APPROVED để tải xuống.");
        }
        document.setDownloadCount(document.getDownloadCount() + 1);
        documentRepository.save(document);
        return OperationResult.ok("Tải xuống mô phỏng thành công. downloadCount đã tăng.", document);
    }
}
