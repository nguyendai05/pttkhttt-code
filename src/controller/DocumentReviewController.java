package controller;

import model.DocumentItem;
import service.DocumentReviewService;
import util.OperationResult;

import java.util.List;


public class DocumentReviewController {
    private DocumentReviewService documentReviewService;

    public DocumentReviewController(DocumentReviewService documentReviewService) {
        this.documentReviewService = documentReviewService;
    }



    public OperationResult<List<DocumentItem>> getPendingDocuments() {
        return documentReviewService.getPendingDocuments();
    }



    public OperationResult<DocumentItem> approve(String documentId) {
        return documentReviewService.approve(documentId);
    }



    public OperationResult<DocumentItem> reject(String documentId, String reason) {
        return documentReviewService.reject(documentId, reason);
    }
}
