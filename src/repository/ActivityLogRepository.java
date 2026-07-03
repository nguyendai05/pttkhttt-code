package repository;

import model.ActivityLog;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


public class ActivityLogRepository {
    private List<ActivityLog> logs = new ArrayList<>();

    public void save(ActivityLog log) {
        logs.add(log);
    }

    public List<ActivityLog> findAll() {
        return logs.stream()
                .sorted(Comparator.comparing((ActivityLog log) -> log.getCreatedAt()).reversed())
                .collect(Collectors.toList());
    }

    public List<ActivityLog> recent(int limit) {
        return findAll().stream().limit(limit).collect(Collectors.toList());
    }
}
