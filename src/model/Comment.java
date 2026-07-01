package model;

import util.DateTimeUtil;

import java.time.LocalDateTime;

/**
 * OWNER: Team 17
 * FEATURE GROUP: Model entity dùng chung
 * PURPOSE: Thành phần nền tảng của phần 1, phục vụ mô phỏng dữ liệu và luồng nghiệp vụ ở các phần sau.
 */
public class Comment {
    public String id;
    public String userId;
    public String targetType;
    public String targetId;
    public String content;
    public boolean hidden;
    public boolean deleted;
    public String moderationReason;
    public LocalDateTime createdAt;

    public Comment(String id, String userId, String targetType, String targetId, String content) {
        this.id = id;
        this.userId = userId;
        this.targetType = targetType;
        this.targetId = targetId;
        this.content = content;
        this.createdAt = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return id + " | user=" + userId + " | " + targetType + ":" + targetId
                + " | hidden=" + hidden + " | deleted=" + deleted
                + " | " + content + " | " + DateTimeUtil.format(createdAt);
    }
}
