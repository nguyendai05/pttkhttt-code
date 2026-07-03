package controller;

import service.ReportService;
import util.OperationResult;


public class ReportController {
    private ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }



    public OperationResult<String> generateReport() {
        return reportService.generateReport();
    }
}
