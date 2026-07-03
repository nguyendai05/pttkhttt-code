package service;

import model.DocumentItem;
import model.UserAccount;
import model.enums.DocumentStatus;
import model.enums.Role;
import repository.DocumentRepository;
import util.IdGenerator;
import util.InputValidator;
import util.OperationResult;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


public class DocumentUploadService {
    private DocumentRepository documentRepository;
    private SessionManager sessionManager;

    public DocumentUploadService(DocumentRepository documentRepository, SessionManager sessionManager) {
        this.documentRepository = documentRepository;
        this.sessionManager = sessionManager;
    }



    public OperationResult<DocumentItem> upload(String fileName, String title, String description, String category,
                                                String authorOrSource, String keywordsCsv, int year, double fileSizeMB) {
        UserAccount currentUser = sessionManager.getCurrentUser().orElse(null);
        if (currentUser == null || currentUser.getRole() != Role.USER) {
            return OperationResult.fail("Chỉ User đã đăng nhập được tải lên tài liệu.");
        }
        if (InputValidator.isBlank(fileName) || InputValidator.isBlank(title) || InputValidator.isBlank(description)
                || InputValidator.isBlank(category) || InputValidator.isBlank(authorOrSource)) {
            return OperationResult.fail("Vui lòng nhập đầy đủ fileName, title, description, category và authorOrSource.");
        }
        if (!InputValidator.hasAllowedDocumentExtension(fileName)) {
            return OperationResult.fail("File chỉ được phép có phần mở rộng: pdf, docx, pptx, txt.");
        }
        if (year <= 0) {
            return OperationResult.fail("Year phải là số dương.");
        }
        if (fileSizeMB <= 0 || fileSizeMB > 50) {
            return OperationResult.fail("Dung lượng mô phỏng phải > 0MB và <= 50MB.");
        }

        List<String> keywords = Arrays.stream((keywordsCsv == null ? "" : keywordsCsv).split(","))
                .map(String::trim)
                .filter(keyword -> !keyword.isEmpty())
                .collect(Collectors.toList());
        DocumentItem document = new DocumentItem(
                IdGenerator.nextId("DOC"),
                title.trim(),
                description.trim(),
                category.trim(),
                authorOrSource.trim(),
                keywords,
                year,
                fileName.trim(),
                currentUser.getId(),
                DocumentStatus.PENDING_REVIEW
        );
        document.setFileSizeMB(fileSizeMB);
        documentRepository.save(document);
        return OperationResult.ok("Tải lên tài liệu thành công. Tài liệu đang chờ Moderator duyệt.", document);
    }
}
