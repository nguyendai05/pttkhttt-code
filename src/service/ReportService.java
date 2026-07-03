package service;

import model.ActivityLog;
import model.DocumentItem;
import model.RequestPost;
import model.UserAccount;
import model.enums.DocumentStatus;
import model.enums.RequestStatus;
import model.enums.Role;
import repository.DocumentRepository;
import repository.RequestPostRepository;
import repository.UserRepository;
import util.OperationResult;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class ReportService {
    private UserRepository userRepository;
    private DocumentRepository documentRepository;
    private RequestPostRepository requestPostRepository;
    private ActivityLogService activityLogService;
    private SessionManager sessionManager;

    public ReportService(UserRepository userRepository, DocumentRepository documentRepository,
                         RequestPostRepository requestPostRepository, ActivityLogService activityLogService,
                         SessionManager sessionManager) {
        this.userRepository = userRepository;
        this.documentRepository = documentRepository;
        this.requestPostRepository = requestPostRepository;
        this.activityLogService = activityLogService;
        this.sessionManager = sessionManager;
    }



    public OperationResult<String> generateReport() {
        UserAccount admin = sessionManager.getCurrentUser().orElse(null);
        if (admin == null || admin.getRole() != Role.ADMIN) {
            return OperationResult.fail("Chỉ Admin được xem báo cáo/thống kê.");
        }

        List<UserAccount> users = userRepository.findAll();
        List<DocumentItem> documents = documentRepository.findAll();
        List<RequestPost> posts = requestPostRepository.findAll();
        Map<Role, Long> usersByRole = users.stream().collect(Collectors.groupingBy(user -> user.getRole(), Collectors.counting()));
        Map<Object, Long> usersByStatus = users.stream().collect(Collectors.groupingBy(user -> user.getStatus(), Collectors.counting()));
        Map<DocumentStatus, Long> documentsByStatus = documents.stream().collect(Collectors.groupingBy(document -> document.getStatus(), Collectors.counting()));
        Map<RequestStatus, Long> postsByStatus = posts.stream().collect(Collectors.groupingBy(post -> post.getStatus(), Collectors.counting()));

        StringBuilder report = new StringBuilder();
        report.append("Tổng số user: ").append(users.size()).append(System.lineSeparator());
        report.append("User theo role: ").append(usersByRole).append(System.lineSeparator());
        report.append("User theo status: ").append(usersByStatus).append(System.lineSeparator());
        report.append("Tổng số tài liệu: ").append(documents.size()).append(System.lineSeparator());
        report.append("Tài liệu theo trạng thái: ").append(documentsByStatus).append(System.lineSeparator());
        report.append("Top tài liệu tải nhiều:").append(System.lineSeparator());
        documentRepository.topDownloaded(5).forEach(document -> report.append(" - ").append(document).append(System.lineSeparator()));
        report.append("Bài yêu cầu theo trạng thái: ").append(postsByStatus).append(System.lineSeparator());
        report.append("ActivityLog gần đây:").append(System.lineSeparator());
        for (ActivityLog log : activityLogService.recentLogs(10)) {
            report.append(" - ").append(log).append(System.lineSeparator());
        }
        return OperationResult.ok("Tạo báo cáo thành công.", report.toString());
    }
}
