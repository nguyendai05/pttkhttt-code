package controller;

import model.DocumentItem;
import service.DocumentUploadService;
import util.OperationResult;


public class DocumentUploadController {
    private DocumentUploadService documentUploadService;

    public DocumentUploadController(DocumentUploadService documentUploadService) {
        this.documentUploadService = documentUploadService;
    }



    public OperationResult<DocumentItem> upload(String fileName, String title, String description, String category,
                                                String authorOrSource, String keywordsCsv, int year, double fileSizeMB) {
        return documentUploadService.upload(fileName, title, description, category, authorOrSource, keywordsCsv, year, fileSizeMB);
    }
}
