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

/**
 * OWNER: Hồ Nguyễn Quốc Nam
 * FEATURE GROUP: Console view cho tài liệu, tìm kiếm, bình luận và rating
 * RELATED USE CASES: UC-5, UC-6, UC-7
 * PURPOSE: Nhập dữ liệu từ console, gọi controller phần 3 và in kết quả ra console.
 */
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

    /**
     * OWNER: Nguyễn Xuân Đại
     * USE CASE: UC-5a - Tải lên tài liệu
     * ACTOR: User
     * FLOW: Basic Flow / Exception Flow
     * PURPOSE: Nhập metadata tài liệu và gọi DocumentUploadController.
     * SEQUENCE NOTE: ConsoleView -> DocumentUploadController -> DocumentUploadService -> DocumentRepository -> SessionManager.
     */
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

    /**
     * OWNER: Nguyễn Xuân Đại
     * USE CASE: UC-5b - Kiểm duyệt tài liệu
     * ACTOR: Moderator
     * FLOW: Basic Flow / Alternative Flow / Exception Flow
     * PURPOSE: In danh sách pending và gọi approve/reject theo lựa chọn.
     * SEQUENCE NOTE: ConsoleView -> DocumentReviewController -> DocumentReviewService -> DocumentRepository/ActivityLogRepository -> SessionManager.
     */
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

    /**
     * OWNER: Hồ Nguyễn Quốc Nam
     * USE CASE: UC-6 - Tra cứu và khai thác tài liệu
     * ACTOR: Guest/User/Moderator/Admin
     * FLOW: Basic Flow / Exception Flow
     * PURPOSE: Tìm kiếm tài liệu APPROVED, xem chi tiết và tải xuống mô phỏng nếu người dùng đã đăng nhập.
     * SEQUENCE NOTE: ConsoleView -> DocumentSearchController -> DocumentSearchService -> DocumentRepository -> SessionManager.
     */
    public void showSearchForm() {
        OperationResult<List<DocumentItem>> result = documentSearchController.searchApproved(prompt("Keyword: "));
        printResult(result);
        if (result.isSuccess()) {
            result.getData().forEach(System.out::println);
        }
    }

    /**
     * OWNER: Hồ Nguyễn Quốc Nam
     * USE CASE: UC-7 - Bình luận và đánh giá tài liệu
     * ACTOR: User/Moderator/Admin
     * FLOW: Basic Flow / Alternative Flow / Exception Flow
     * PURPOSE: Gọi các controller comment/rating theo lựa chọn console.
     * SEQUENCE NOTE: ConsoleView -> DocumentInteractionController -> CommentService/RatingService -> Repository -> SessionManager.
     */
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
