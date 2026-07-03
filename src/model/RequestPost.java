package model;

import model.enums.RequestStatus;
import util.DateTimeUtil;

import java.time.LocalDateTime;


public class RequestPost {
    private String id;
    private String title;
    private String content;
    private String creatorId;
    private RequestStatus status;
    private String rejectionReason;
    private String linkedDocumentId;
    private boolean hidden;
    private boolean deleted;
    private LocalDateTime createdAt;

    public RequestPost(String id, String title, String content, String creatorId, RequestStatus status) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.creatorId = creatorId;
        this.status = status;
        this.createdAt = LocalDateTime.now();
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    public RequestStatus getStatus() {
        return status;
    }

    public void setStatus(RequestStatus status) {
        this.status = status;
    }

    public String getRejectionReason() {
        return rejectionReason;
    }

    public void setRejectionReason(String rejectionReason) {
        this.rejectionReason = rejectionReason;
    }

    public String getLinkedDocumentId() {
        return linkedDocumentId;
    }

    public void setLinkedDocumentId(String linkedDocumentId) {
        this.linkedDocumentId = linkedDocumentId;
    }

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return id + " | " + title + " | creator=" + creatorId + " | " + status
                + " | hidden=" + hidden + " | deleted=" + deleted
                + " | " + DateTimeUtil.format(createdAt);
    }
}
