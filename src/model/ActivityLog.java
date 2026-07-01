package model;

import util.DateTimeUtil;

import java.time.LocalDateTime;

/**
 * OWNER: Team 17
 * FEATURE GROUP: Model entity dùng chung
 * PURPOSE: Thành phần nền tảng của phần 1, phục vụ mô phỏng dữ liệu và luồng nghiệp vụ ở các phần sau.
 */
public class ActivityLog {
    public String id;
    public String actorId;
    public String action;
    public String targetType;
    public String targetId;
    public String description;
    public LocalDateTime createdAt;

    public ActivityLog(String id, String actorId, String action, String targetType, String targetId, String description) {
        this.id = id;
        this.actorId = actorId;
        this.action = action;
        this.targetType = targetType;
        this.targetId = targetId;
        this.description = description;
        this.createdAt = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return DateTimeUtil.format(createdAt) + " | actor=" + actorId + " | " + action
                + " | " + targetType + ":" + targetId + " | " + description;
    }
}
