package repository;

import model.DocumentItem;
import model.enums.DocumentStatus;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * OWNER: Team 17
 * FEATURE GROUP: Repository in-memory dùng chung
 * PURPOSE: Thành phần nền tảng của phần 1, phục vụ mô phỏng dữ liệu và luồng nghiệp vụ ở các phần sau.
 */
public class DocumentRepository {
    private final List<DocumentItem> documents = new ArrayList<>();

    public void save(DocumentItem document) {
        findById(document.id).ifPresent(documents::remove);
        documents.add(document);
    }

    public Optional<DocumentItem> findById(String id) {
        return documents.stream().filter(document -> document.id.equalsIgnoreCase(id)).findFirst();
    }

    public List<DocumentItem> findAll() {
        return new ArrayList<>(documents);
    }

    public List<DocumentItem> findByStatus(DocumentStatus status) {
        return documents.stream().filter(document -> document.status == status).collect(Collectors.toList());
    }

    public List<DocumentItem> findByUploader(String uploaderId) {
        return documents.stream().filter(document -> document.uploaderId.equals(uploaderId)).collect(Collectors.toList());
    }

    public List<DocumentItem> searchApproved(String keyword) {
        return documents.stream()
                .filter(document -> document.status == DocumentStatus.APPROVED)
                .filter(document -> document.matches(keyword))
                .collect(Collectors.toList());
    }

    public List<DocumentItem> topDownloaded(int limit) {
        return documents.stream()
                .sorted(Comparator.comparingInt((DocumentItem document) -> document.downloadCount).reversed())
                .limit(limit)
                .collect(Collectors.toList());
    }
}
