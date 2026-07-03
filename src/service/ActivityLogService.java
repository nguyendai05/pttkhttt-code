package service;

import model.ActivityLog;
import repository.ActivityLogRepository;
import util.IdGenerator;

import java.util.List;


public class ActivityLogService {
    private ActivityLogRepository activityLogRepository;

    public ActivityLogService(ActivityLogRepository activityLogRepository) {
        this.activityLogRepository = activityLogRepository;
    }



    public void log(String actorId, String action, String targetType, String targetId, String description) {
        activityLogRepository.save(new ActivityLog(IdGenerator.nextId("LOG"), actorId, action, targetType, targetId, description));
    }



    public List<ActivityLog> recentLogs(int limit) {
        return activityLogRepository.recent(limit);
    }
}
