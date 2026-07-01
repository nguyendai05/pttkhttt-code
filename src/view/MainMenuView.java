package view;

import app.AppContext;
import model.UserAccount;
import model.enums.AccountStatus;
import model.enums.CollectionVisibility;
import model.enums.Role;
import util.OperationResult;

import java.util.List;
import java.util.Scanner;

/**
 * OWNER: Team 17
 * FEATURE GROUP: Console menu chính
 * RELATED USE CASES: UC-1, UC-2, UC-3, UC-4, UC-5, UC-6, UC-7, UC-8, UC-9, UC-10
 * PURPOSE: Điều phối menu console theo trạng thái đăng nhập và role hiện tại.
 */
public class MainMenuView {
    private final AppContext context;
    private final Scanner scanner = new Scanner(System.in, "UTF-8");
    private boolean running = true;

    public MainMenuView(AppContext context) {
        this.context = context;
    }

    /**
     * OWNER: Team 17
     * USE CASE: Console Shell - Menu chính
     * ACTOR: Guest/User/Moderator/Admin
     * FLOW: Basic Flow
     * PURPOSE: Chọn menu Guest/User/Moderator/Admin theo currentUser trong SessionManager.
     * SEQUENCE NOTE: ConsoleView -> Controller -> Service -> Repository -> SessionManager.
     */
    public void start() {
        while (running) {
            UserAccount currentUser = context.sessionManager.getCurrentUser().orElse(null);
            if (currentUser == null) {
                showGuestMenu();
            } else if (currentUser.role == Role.USER) {
                showUserMenu();
            } else if (currentUser.role == Role.MODERATOR) {
                showModeratorMenu();
            } else if (currentUser.role == Role.ADMIN) {
                showAdminMenu();
            } else {
                printLine("Role không hợp lệ, tự động đăng xuất.");
                context.authController.logout();
            }
        }
        printLine("Đã thoát chương trình.");
    }

    private void showGuestMenu() {
        printLine("\n=== MENU GUEST ===");
        printLine("1. Đăng ký");
        printLine("2. Đăng nhập");
        printLine("3. Quên mật khẩu");
        printLine("4. Tra cứu tài liệu");
        printLine("0. Thoát");
        switch (readInt("Chọn: ")) {
            case 1:
                register();
                break;
            case 2:
                login();
                break;
            case 3:
                recoverPassword();
                break;
            case 4:
                searchDocuments();
                break;
            case 0:
                running = false;
                break;
            default:
                printLine("Lựa chọn không hợp lệ.");
        }
    }

    private void showUserMenu() {
        printLine("\n=== MENU USER ===");
        printLine("1. Tải lên tài liệu");
        printLine("2. Tra cứu tài liệu");
        printLine("3. Bình luận/đánh giá tài liệu");
        printLine("4. Thư viện cá nhân");
        printLine("5. Tạo bài yêu cầu tài liệu");
        printLine("6. Xem bài yêu cầu của tôi");
        printLine("0. Đăng xuất");
        switch (readInt("Chọn: ")) {
            case 1:
                uploadDocument();
                break;
            case 2:
                searchDocuments();
                break;
            case 3:
                interactWithDocument();
                break;
            case 4:
                personalLibraryMenu();
                break;
            case 5:
                createRequestPost();
                break;
            case 6:
                myRequestPosts();
                break;
            case 0:
                printResult(context.authController.logout());
                break;
            default:
                printLine("Lựa chọn không hợp lệ.");
        }
    }

    private void showModeratorMenu() {
        printLine("\n=== MENU MODERATOR ===");
        printLine("1. Kiểm duyệt tài liệu");
        printLine("2. Tra cứu tài liệu");
        printLine("3. Kiểm soát nội dung forum");
        printLine("4. Xem danh sách người dùng");
        printLine("0. Đăng xuất");
        switch (readInt("Chọn: ")) {
            case 1:
                reviewDocuments();
                break;
            case 2:
                searchDocuments();
                break;
            case 3:
                moderateForum();
                break;
            case 4:
                viewUsers();
                break;
            case 0:
                printResult(context.authController.logout());
                break;
            default:
                printLine("Lựa chọn không hợp lệ.");
        }
    }

    private void showAdminMenu() {
        printLine("\n=== MENU ADMIN ===");
        printLine("1. Quản trị người dùng");
        printLine("2. Duyệt bài yêu cầu tài liệu");
        printLine("3. Báo cáo/thống kê");
        printLine("4. Xem Activity Log");
        printLine("0. Đăng xuất");
        switch (readInt("Chọn: ")) {
            case 1:
                manageUsers();
                break;
            case 2:
                reviewRequestPosts();
                break;
            case 3:
                printResult(context.reportController.generateReport());
                break;
            case 4:
                context.activityLogService.recentLogs(20).forEach(System.out::println);
                break;
            case 0:
                printResult(context.authController.logout());
                break;
            default:
                printLine("Lựa chọn không hợp lệ.");
        }
    }

    private void register() {
        printLine("\n--- UC-1 Đăng ký ---");
        printResult(context.registrationController.register(
                prompt("Username: "),
                prompt("Email: "),
                prompt("Password: "),
                prompt("Confirm password: ")
        ));
    }

    private void login() {
        printLine("\n--- UC-3 Đăng nhập ---");
        printResult(context.authController.login(prompt("Username/email: "), prompt("Password: ")));
    }

    private void recoverPassword() {
        printLine("\n--- UC-2 Quên mật khẩu ---");
        String email = prompt("Email: ");
        OperationResult<?> otpResult = context.passwordRecoveryController.requestOtp(email);
        printResult(otpResult);
        if (!otpResult.isSuccess()) {
            return;
        }
        Object data = otpResult.getData();
        if (data instanceof model.OtpRequest) {
            printLine("OTP mô phỏng gửi email: " + ((model.OtpRequest) data).otpCode);
        }
        printResult(context.passwordRecoveryController.resetPassword(
                email,
                prompt("OTP: "),
                prompt("Password mới: "),
                prompt("Confirm password mới: ")
        ));
    }

    private void uploadDocument() {
        printLine("\n--- UC-5a Tải lên tài liệu ---");
        printResult(context.documentUploadController.upload(
                prompt("File name (pdf/docx/pptx/txt): "),
                prompt("Title: "),
                prompt("Description: "),
                prompt("Category: "),
                prompt("Author/source: "),
                prompt("Keywords CSV: "),
                readInt("Year: "),
                readDouble("File size MB: ")
        ));
    }

    private void reviewDocuments() {
        printLine("\n--- UC-5b Kiểm duyệt tài liệu ---");
        printResult(context.documentReviewController.getPendingDocuments());
        String documentId = prompt("Document ID cần xử lý (Enter để bỏ qua): ");
        if (documentId.trim().isEmpty()) {
            return;
        }
        String action = prompt("A=approve, R=reject: ");
        if ("A".equalsIgnoreCase(action)) {
            printResult(context.documentReviewController.approve(documentId));
        } else if ("R".equalsIgnoreCase(action)) {
            printResult(context.documentReviewController.reject(documentId, prompt("Lý do từ chối: ")));
        } else {
            printLine("Hành động không hợp lệ.");
        }
    }

    private void searchDocuments() {
        printLine("\n--- UC-6 Tra cứu tài liệu ---");
        printResult(context.documentSearchController.searchApproved(prompt("Từ khóa: ")));
        if (!context.sessionManager.isLoggedIn()) {
            return;
        }
        String documentId = prompt("Document ID để xem chi tiết/tải xuống (Enter để bỏ qua): ");
        if (documentId.trim().isEmpty()) {
            return;
        }
        printResult(context.documentSearchController.viewDetail(documentId));
        if ("Y".equalsIgnoreCase(prompt("Tải xuống mô phỏng? (Y/N): "))) {
            printResult(context.documentSearchController.download(documentId));
        }
    }

    private void interactWithDocument() {
        printLine("\n--- UC-7 Bình luận/đánh giá tài liệu ---");
        String documentId = prompt("Document ID: ");
        printLine("1. Thêm bình luận");
        printLine("2. Đánh giá 1-5");
        printLine("3. Xem bình luận");
        printLine("4. Sửa bình luận của tôi");
        printLine("5. Xóa bình luận của tôi");
        switch (readInt("Chọn: ")) {
            case 1:
                printResult(context.documentInteractionController.addComment(documentId, prompt("Nội dung: ")));
                break;
            case 2:
                printResult(context.documentInteractionController.rateDocument(documentId, readInt("Điểm: ")));
                break;
            case 3:
                printResult(context.documentInteractionController.getDocumentComments(documentId));
                break;
            case 4:
                printResult(context.documentInteractionController.updateOwnComment(prompt("Comment ID: "), prompt("Nội dung mới: ")));
                break;
            case 5:
                printResult(context.documentInteractionController.deleteOwnComment(prompt("Comment ID: ")));
                break;
            default:
                printLine("Lựa chọn không hợp lệ.");
        }
    }

    private void personalLibraryMenu() {
        printLine("\n--- UC-9 Thư viện cá nhân ---");
        printLine("1. Xem profile");
        printLine("2. Cập nhật profile");
        printLine("3. Xem tài liệu mình upload");
        printLine("4. Lưu tài liệu APPROVED");
        printLine("5. Xem thư viện cá nhân");
        printLine("6. Tạo collection");
        switch (readInt("Chọn: ")) {
            case 1:
                printResult(context.personalLibraryController.viewProfile());
                break;
            case 2:
                printResult(context.personalLibraryController.updateProfile(prompt("Full name: "), prompt("Bio: ")));
                break;
            case 3:
                printResult(context.personalLibraryController.myUploadedDocuments());
                break;
            case 4:
                printResult(context.personalLibraryController.saveApprovedDocument(prompt("Document ID: ")));
                break;
            case 5:
                printResult(context.personalLibraryController.viewSavedDocuments());
                break;
            case 6:
                printResult(context.personalLibraryController.createCollection(prompt("Tên collection: "), parseVisibility(prompt("PRIVATE/SHARED: "))));
                break;
            default:
                printLine("Lựa chọn không hợp lệ.");
        }
    }

    private void createRequestPost() {
        printLine("\n--- UC-10a Tạo bài yêu cầu tài liệu ---");
        printResult(context.requestPostController.createPost(prompt("Title: "), prompt("Content: ")));
    }

    private void myRequestPosts() {
        printLine("\n--- UC-10a Bài yêu cầu của tôi ---");
        printResult(context.requestPostController.myPosts());
        printLine("1. Sửa bài");
        printLine("2. Xóa bài");
        printLine("3. Bình luận vào bài OPEN");
        printLine("4. Đánh dấu FULFILLED");
        printLine("0. Quay lại");
        switch (readInt("Chọn: ")) {
            case 1:
                printResult(context.requestPostController.updateOwnPost(prompt("Request ID: "), prompt("Title mới: "), prompt("Content mới: ")));
                break;
            case 2:
                printResult(context.requestPostController.deleteOwnPost(prompt("Request ID: ")));
                break;
            case 3:
                printResult(context.requestPostController.commentOpenPost(prompt("Request ID: "), prompt("Nội dung bình luận: ")));
                break;
            case 4:
                printResult(context.requestPostController.markFulfilled(prompt("Request ID: "), prompt("Linked documentId (có thể bỏ trống): ")));
                break;
            case 0:
                break;
            default:
                printLine("Lựa chọn không hợp lệ.");
        }
    }

    private void manageUsers() {
        printLine("\n--- UC-4 Quản trị người dùng ---");
        viewUsers();
        printLine("1. Đổi role USER/MODERATOR");
        printLine("2. Khóa/mở khóa tài khoản");
        printLine("0. Quay lại");
        switch (readInt("Chọn: ")) {
            case 1:
                Role role = parseRole(prompt("Role mới USER/MODERATOR: "));
                if (role == null) {
                    printLine("Role không hợp lệ.");
                    return;
                }
                printResult(context.userManagementController.changeRole(prompt("User ID: "), role));
                break;
            case 2:
                AccountStatus status = parseStatus(prompt("Status mới ACTIVE/LOCKED: "));
                if (status == null) {
                    printLine("Status không hợp lệ.");
                    return;
                }
                printResult(context.userManagementController.changeStatus(prompt("User ID: "), status));
                break;
            case 0:
                break;
            default:
                printLine("Lựa chọn không hợp lệ.");
        }
    }

    private void viewUsers() {
        String keyword = prompt("Từ khóa user (Enter để bỏ qua): ");
        printResult(context.userManagementController.searchUsers(keyword, null, null));
    }

    private void reviewRequestPosts() {
        printLine("\n--- UC-10b Duyệt bài yêu cầu tài liệu ---");
        OperationResult<?> pendingResult = context.requestPostController.pendingPosts();
        printResult(pendingResult);
        if (!pendingResult.isSuccess()) {
            return;
        }
        String postId = prompt("Request ID cần xử lý (Enter để bỏ qua): ");
        if (postId.trim().isEmpty()) {
            return;
        }
        String action = prompt("A=approve, R=reject: ");
        if ("A".equalsIgnoreCase(action)) {
            printResult(context.requestPostController.approvePost(postId));
        } else if ("R".equalsIgnoreCase(action)) {
            printResult(context.requestPostController.rejectPost(postId, prompt("Lý do từ chối: ")));
        } else {
            printLine("Hành động không hợp lệ.");
        }
    }

    private void moderateForum() {
        printLine("\n--- UC-10c Kiểm soát nội dung forum ---");
        printResult(context.forumModerationController.viewForumContent());
        printLine("1. Ẩn bài viết");
        printLine("2. Xóa bài viết");
        printLine("3. Ẩn bình luận");
        printLine("4. Xóa bình luận");
        switch (readInt("Chọn: ")) {
            case 1:
                printResult(context.forumModerationController.moderatePost(prompt("Request ID: "), false, prompt("Lý do: ")));
                break;
            case 2:
                printResult(context.forumModerationController.moderatePost(prompt("Request ID: "), true, prompt("Lý do: ")));
                break;
            case 3:
                printResult(context.forumModerationController.moderateComment(prompt("Comment ID: "), false, prompt("Lý do: ")));
                break;
            case 4:
                printResult(context.forumModerationController.moderateComment(prompt("Comment ID: "), true, prompt("Lý do: ")));
                break;
            default:
                printLine("Lựa chọn không hợp lệ.");
        }
    }

    private String prompt(String label) {
        System.out.print(label);
        if (!scanner.hasNextLine()) {
            running = false;
            return "";
        }
        return scanner.nextLine().trim();
    }

    private int readInt(String label) {
        String value = prompt(label);
        if (value.isEmpty()) {
            return -1;
        }
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException exception) {
            return -1;
        }
    }

    private double readDouble(String label) {
        String value = prompt(label);
        if (value.isEmpty()) {
            return -1;
        }
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException exception) {
            return -1;
        }
    }

    private Role parseRole(String value) {
        try {
            return Role.valueOf(value.trim().toUpperCase());
        } catch (Exception exception) {
            return null;
        }
    }

    private AccountStatus parseStatus(String value) {
        try {
            return AccountStatus.valueOf(value.trim().toUpperCase());
        } catch (Exception exception) {
            return null;
        }
    }

    private CollectionVisibility parseVisibility(String value) {
        try {
            return CollectionVisibility.valueOf(value.trim().toUpperCase());
        } catch (Exception exception) {
            return CollectionVisibility.PRIVATE;
        }
    }

    private void printResult(OperationResult<?> result) {
        printLine((result.isSuccess() ? "[OK] " : "[FAIL] ") + result.getMessage());
        Object data = result.getData();
        if (data instanceof List<?>) {
            List<?> list = (List<?>) data;
            if (list.isEmpty()) {
                printLine("(Danh sách rỗng)");
            } else {
                list.forEach(System.out::println);
            }
        } else if (data != null) {
            printLine(String.valueOf(data));
        }
    }

    private void printLine(String text) {
        System.out.println(text);
    }
}
