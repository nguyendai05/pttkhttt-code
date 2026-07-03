package controller;

import model.DocumentItem;
import service.DocumentSearchService;
import util.OperationResult;

import java.util.List;


public class DocumentSearchController {
    private DocumentSearchService documentSearchService;

    public DocumentSearchController(DocumentSearchService documentSearchService) {
        this.documentSearchService = documentSearchService;
    }



    public OperationResult<List<DocumentItem>> searchApproved(String keyword) {
        return documentSearchService.searchApproved(keyword);
    }



    public OperationResult<DocumentItem> viewDetail(String documentId) {
        return documentSearchService.viewDetail(documentId);
    }



    public OperationResult<DocumentItem> download(String documentId) {
        return documentSearchService.download(documentId);
    }
}
