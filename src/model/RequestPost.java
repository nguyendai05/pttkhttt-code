package model;

import model.enums.RequestStatus;
import util.DateTimeUtil;

import java.time.LocalDateTime;

/**
 * OWNER: Team 17
 * FEATURE GROUP: Model entity dùng chung
 * PURPOSE: Thành phần nền tảng của phần 1, phục vụ mô phỏng dữ liệu và luồng nghiệp vụ ở các phần sau.
 */
public class RequestPost {
    public String id;
    public String title;
    public String content;
    public String creatorId;
    public RequestStatus status;
    public String rejectionReason;
    public String linkedDocumentId;
    public boolean hidden;
    public boolean deleted;
    public LocalDateTime createdAt;

    public RequestPost(String id, String title, String content, String creatorId, RequestStatus status) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.creatorId = creatorId;
        this.status = status;
        this.createdAt = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return id + " | " + title + " | creator=" + creatorId + " | " + status
                + " | hidden=" + hidden + " | deleted=" + deleted
                + " | " + DateTimeUtil.format(createdAt);
    }
}
