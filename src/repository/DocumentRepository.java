package repository;

import model.DocumentItem;
import model.enums.DocumentStatus;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


public class DocumentRepository {
    private List<DocumentItem> documents = new ArrayList<>();

    public void save(DocumentItem document) {
        findById(document.getId()).ifPresent(documents::remove);
        documents.add(document);
    }

    public Optional<DocumentItem> findById(String id) {
        return documents.stream().filter(document -> document.getId().equalsIgnoreCase(id)).findFirst();
    }

    public List<DocumentItem> findAll() {
        return new ArrayList<>(documents);
    }

    public List<DocumentItem> findByStatus(DocumentStatus status) {
        return documents.stream().filter(document -> document.getStatus() == status).collect(Collectors.toList());
    }

    public List<DocumentItem> findByUploader(String uploaderId) {
        return documents.stream().filter(document -> document.getUploaderId().equals(uploaderId)).collect(Collectors.toList());
    }

    public List<DocumentItem> searchApproved(String keyword) {
        return documents.stream()
                .filter(document -> document.getStatus() == DocumentStatus.APPROVED)
                .filter(document -> document.matches(keyword))
                .collect(Collectors.toList());
    }

    public List<DocumentItem> topDownloaded(int limit) {
        return documents.stream()
                .sorted(Comparator.comparingInt((DocumentItem document) -> document.getDownloadCount()).reversed())
                .limit(limit)
                .collect(Collectors.toList());
    }
}
