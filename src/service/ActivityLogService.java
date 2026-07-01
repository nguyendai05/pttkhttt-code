package service;

import model.ActivityLog;
import repository.ActivityLogRepository;
import util.IdGenerator;

import java.util.List;

/**
 * OWNER: Nguyễn Minh Luân
 * FEATURE GROUP: Activity Log
 * RELATED USE CASES: UC-4, UC-8, UC-10
 * PURPOSE: Ghi và đọc ActivityLog phục vụ quản trị, báo cáo và kiểm soát nội dung.
 */
public class ActivityLogService {
    private final ActivityLogRepository activityLogRepository;

    public ActivityLogService(ActivityLogRepository activityLogRepository) {
        this.activityLogRepository = activityLogRepository;
    }

    /**
     * OWNER: Nguyễn Minh Luân
     * USE CASE: UC-4/UC-8/UC-10 - Ghi ActivityLog
     * ACTOR: Admin/Moderator/System
     * FLOW: Basic Flow
     * PURPOSE: Tạo bản ghi ActivityLog cho hành động quản trị hoặc kiểm duyệt.
     * SEQUENCE NOTE: ConsoleView -> Controller -> Service -> ActivityLogRepository -> SessionManager.
     */
    public void log(String actorId, String action, String targetType, String targetId, String description) {
        activityLogRepository.save(new ActivityLog(IdGenerator.nextId("LOG"), actorId, action, targetType, targetId, description));
    }

    /**
     * OWNER: Nguyễn Minh Luân
     * USE CASE: UC-8 - ActivityLog gần đây
     * ACTOR: Admin
     * FLOW: Basic Flow
     * PURPOSE: Lấy danh sách ActivityLog gần nhất cho báo cáo/thống kê.
     * SEQUENCE NOTE: ConsoleView -> ReportController -> ReportService -> ActivityLogRepository -> SessionManager.
     */
    public List<ActivityLog> recentLogs(int limit) {
        return activityLogRepository.recent(limit);
    }
}
