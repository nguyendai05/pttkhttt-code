package model;

import model.enums.DocumentStatus;
import util.DateTimeUtil;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * OWNER: Team 17
 * FEATURE GROUP: Model entity dùng chung
 * PURPOSE: Thành phần nền tảng của phần 1, phục vụ mô phỏng dữ liệu và luồng nghiệp vụ ở các phần sau.
 */
public class DocumentItem {
    private String id;
    private String title;
    private String description;
    private String category;
    private String authorOrSource;
    private List<String> keywords = new ArrayList<>();
    private int year;
    private String fileName;
    private double fileSizeMB;
    private String uploaderId;
    private DocumentStatus status;
    private String rejectionReason;
    private String approvedBy;
    private LocalDateTime approvedAt;
    private LocalDateTime uploadedAt;
    private int downloadCount;

    public DocumentItem(String id, String title, String description, String category, String authorOrSource,
                        List<String> keywords, int year, String fileName, String uploaderId, DocumentStatus status) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.category = category;
        this.authorOrSource = authorOrSource;
        this.keywords = keywords;
        this.year = year;
        this.fileName = fileName;
        this.uploaderId = uploaderId;
        this.status = status;
        this.uploadedAt = LocalDateTime.now();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getAuthorOrSource() {
        return authorOrSource;
    }

    public void setAuthorOrSource(String authorOrSource) {
        this.authorOrSource = authorOrSource;
    }

    public List<String> getKeywords() {
        return keywords;
    }

    public void setKeywords(List<String> keywords) {
        this.keywords = keywords;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public double getFileSizeMB() {
        return fileSizeMB;
    }

    public void setFileSizeMB(double fileSizeMB) {
        this.fileSizeMB = fileSizeMB;
    }

    public String getUploaderId() {
        return uploaderId;
    }

    public void setUploaderId(String uploaderId) {
        this.uploaderId = uploaderId;
    }

    public DocumentStatus getStatus() {
        return status;
    }

    public void setStatus(DocumentStatus status) {
        this.status = status;
    }

    public String getRejectionReason() {
        return rejectionReason;
    }

    public void setRejectionReason(String rejectionReason) {
        this.rejectionReason = rejectionReason;
    }

    public String getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(String approvedBy) {
        this.approvedBy = approvedBy;
    }

    public LocalDateTime getApprovedAt() {
        return approvedAt;
    }

    public void setApprovedAt(LocalDateTime approvedAt) {
        this.approvedAt = approvedAt;
    }

    public LocalDateTime getUploadedAt() {
        return uploadedAt;
    }

    public void setUploadedAt(LocalDateTime uploadedAt) {
        this.uploadedAt = uploadedAt;
    }

    public int getDownloadCount() {
        return downloadCount;
    }

    public void setDownloadCount(int downloadCount) {
        this.downloadCount = downloadCount;
    }

    public boolean matches(String keyword) {
        String lower = keyword == null ? "" : keyword.toLowerCase();
        return title.toLowerCase().contains(lower)
                || description.toLowerCase().contains(lower)
                || category.toLowerCase().contains(lower)
                || authorOrSource.toLowerCase().contains(lower)
                || String.valueOf(year).contains(lower)
                || keywords.stream().anyMatch(item -> item.toLowerCase().contains(lower));
    }

    @Override
    public String toString() {
        String result = id + " | " + title + " | " + category + " | " + year + " | " + status
                + " | sizeMB=" + fileSizeMB + " | downloads=" + downloadCount + " | uploaded=" + DateTimeUtil.format(uploadedAt);
        if (rejectionReason != null && !rejectionReason.trim().isEmpty()) {
            result += " | rejectionReason=" + rejectionReason;
        }
        return result;
    }
}
