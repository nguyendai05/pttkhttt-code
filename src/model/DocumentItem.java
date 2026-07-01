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
    public String id;
    public String title;
    public String description;
    public String category;
    public String authorOrSource;
    public List<String> keywords = new ArrayList<>();
    public int year;
    public String fileName;
    public double fileSizeMB;
    public String uploaderId;
    public DocumentStatus status;
    public String rejectionReason;
    public String approvedBy;
    public LocalDateTime approvedAt;
    public LocalDateTime uploadedAt;
    public int downloadCount;

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
