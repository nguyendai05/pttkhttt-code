package controller;

import service.ReportService;
import util.OperationResult;

/**
 * OWNER: Nguyễn Minh Luân
 * FEATURE GROUP: Báo cáo / Thống kê
 * RELATED USE CASES: UC-8
 * PURPOSE: Nhận request báo cáo từ ConsoleView và gọi ReportService.
 */
public class ReportController {
    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    /**
     * OWNER: Nguyễn Minh Luân
     * USE CASE: UC-8 - Báo cáo / Thống kê
     * ACTOR: Admin
     * FLOW: Basic Flow / Exception Flow
     * PURPOSE: Điều phối tạo báo cáo tổng hợp cho Admin.
     * SEQUENCE NOTE: ConsoleView -> ReportController -> ReportService -> UserRepository/DocumentRepository/RequestPostRepository/ActivityLogRepository -> SessionManager.
     */
    public OperationResult<String> generateReport() {
        return reportService.generateReport();
    }
}
