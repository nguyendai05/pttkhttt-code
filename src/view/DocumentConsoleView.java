package view;

import controller.DocumentInteractionController;
import controller.DocumentReviewController;
import controller.DocumentSearchController;
import controller.DocumentUploadController;
import model.Comment;
import model.DocumentItem;
import util.OperationResult;

import java.util.List;
import java.util.Scanner;


public class DocumentConsoleView {
    private DocumentUploadController documentUploadController;
    private DocumentReviewController documentReviewController;
    private DocumentSearchController documentSearchController;
    private DocumentInteractionController documentInteractionController;
    private Scanner scanner = new Scanner(System.in, "UTF-8");

    public DocumentConsoleView(DocumentUploadController documentUploadController,
                               DocumentReviewController documentReviewController,
                               DocumentSearchController documentSearchController,
                               DocumentInteractionController documentInteractionController) {
        this.documentUploadController = documentUploadController;
        this.documentReviewController = documentReviewController;
        this.documentSearchController = documentSearchController;
        this.documentInteractionController = documentInteractionController;
    }



    public void showUploadForm() {
        printResult(documentUploadController.upload(
                prompt("File name: "),
                prompt("Title: "),
                prompt("Description: "),
                prompt("Category: "),
                prompt("Author/source: "),
                prompt("Keywords CSV: "),
                readInt("Year: "),
                readDouble("File size MB: ")
        ));
    }



    public void showReviewForm() {
        OperationResult<List<DocumentItem>> pending = documentReviewController.getPendingDocuments();
        printResult(pending);
        if (!pending.isSuccess()) {
            return;
        }
        pending.getData().forEach(System.out::println);
        String documentId = prompt("Document ID: ");
        String action = prompt("A=approve, R=reject: ");
        if ("A".equalsIgnoreCase(action)) {
            printResult(documentReviewController.approve(documentId));
        } else if ("R".equalsIgnoreCase(action)) {
            printResult(documentReviewController.reject(documentId, prompt("Reason: ")));
        }
    }



    public void showSearchForm() {
        OperationResult<List<DocumentItem>> result = documentSearchController.searchApproved(prompt("Keyword: "));
        printResult(result);
        if (result.isSuccess()) {
            result.getData().forEach(System.out::println);
        }
    }



    public void showInteractionForm() {
        String documentId = prompt("Document ID: ");
        System.out.println("1. Add comment");
        System.out.println("2. Rate document");
        System.out.println("3. List comments");
        int choice = readInt("Choice: ");
        if (choice == 1) {
            printResult(documentInteractionController.addComment(documentId, prompt("Content: ")));
        } else if (choice == 2) {
            printResult(documentInteractionController.rateDocument(documentId, readInt("Score 1-5: ")));
        } else if (choice == 3) {
            OperationResult<List<Comment>> comments = documentInteractionController.getDocumentComments(documentId);
            printResult(comments);
            if (comments.isSuccess()) {
                comments.getData().forEach(System.out::println);
            }
        }
    }

    private String prompt(String label) {
        System.out.print(label);
        return scanner.nextLine();
    }

    private int readInt(String label) {
        try {
            return Integer.parseInt(prompt(label).trim());
        } catch (NumberFormatException exception) {
            return -1;
        }
    }

    private double readDouble(String label) {
        try {
            return Double.parseDouble(prompt(label).trim());
        } catch (NumberFormatException exception) {
            return -1;
        }
    }

    private void printResult(OperationResult<?> result) {
        System.out.println((result.isSuccess() ? "[OK] " : "[FAIL] ") + result.getMessage());
        if (result.getData() != null && !(result.getData() instanceof List<?>)) {
            System.out.println(result.getData());
        }
    }
}
