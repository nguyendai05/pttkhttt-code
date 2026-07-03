package service;

import model.DocumentItem;
import model.UserAccount;
import model.enums.DocumentStatus;
import repository.DocumentRepository;
import util.OperationResult;

import java.util.List;


public class DocumentSearchService {
    private DocumentRepository documentRepository;
    private SessionManager sessionManager;

    public DocumentSearchService(DocumentRepository documentRepository, SessionManager sessionManager) {
        this.documentRepository = documentRepository;
        this.sessionManager = sessionManager;
    }



    public OperationResult<List<DocumentItem>> searchApproved(String keyword) {
        return OperationResult.ok("Kết quả tìm kiếm tài liệu APPROVED.", documentRepository.searchApproved(keyword));
    }



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
