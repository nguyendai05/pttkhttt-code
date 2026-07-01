package view;

import controller.AuthController;
import controller.PasswordRecoveryController;
import controller.RegistrationController;
import model.OtpRequest;
import util.OperationResult;

import java.util.Scanner;

/**
 * OWNER: Huỳnh Duy Tâm
 * FEATURE GROUP: Console view cho đăng ký, đăng nhập và khôi phục mật khẩu
 * RELATED USE CASES: UC-1, UC-2, UC-3
 * PURPOSE: Nhập dữ liệu từ console, in kết quả và gọi controller tương ứng.
 */
public class AuthConsoleView {
    private final RegistrationController registrationController;
    private final AuthController authController;
    private final PasswordRecoveryController passwordRecoveryController;
    private final Scanner scanner;

    public AuthConsoleView(RegistrationController registrationController,
                           AuthController authController,
                           PasswordRecoveryController passwordRecoveryController) {
        this.registrationController = registrationController;
        this.authController = authController;
        this.passwordRecoveryController = passwordRecoveryController;
        this.scanner = new Scanner(System.in, "UTF-8");
    }

    /**
     * OWNER: Nguyễn Xuân Đại
     * USE CASE: UC-1 - Đăng ký
     * ACTOR: Guest
     * FLOW: Basic Flow / Exception Flow
     * PURPOSE: Nhập username/email/password/confirmPassword và gọi RegistrationController.
     * SEQUENCE NOTE: ConsoleView -> RegistrationController -> RegistrationService -> UserRepository/ProfileRepository -> SessionManager.
     */
    public void showRegisterForm() {
        System.out.println("\n--- UC-1 Đăng ký ---");
        printResult(registrationController.register(
                prompt("Username: "),
                prompt("Email: "),
                prompt("Password: "),
                prompt("Confirm password: ")
        ));
    }

    /**
     * OWNER: Huỳnh Duy Tâm
     * USE CASE: UC-2 - Khôi phục mật khẩu bằng OTP
     * ACTOR: Guest
     * FLOW: Basic Flow / Exception Flow
     * PURPOSE: Nhập email, in OTP ra console, nhập OTP/password mới và gọi PasswordRecoveryController.
     * SEQUENCE NOTE: ConsoleView -> PasswordRecoveryController -> PasswordRecoveryService -> UserRepository/OtpRepository -> SessionManager.
     */
    public void showPasswordRecoveryForm() {
        System.out.println("\n--- UC-2 Khôi phục mật khẩu bằng OTP ---");
        String email = prompt("Email: ");
        OperationResult<OtpRequest> otpResult = passwordRecoveryController.requestOtp(email);
        printResult(otpResult);
        if (!otpResult.isSuccess()) {
            return;
        }
        System.out.println("OTP mô phỏng gửi email: " + otpResult.getData().otpCode);
        printResult(passwordRecoveryController.resetPassword(
                email,
                prompt("OTP: "),
                prompt("Password mới: "),
                prompt("Confirm password mới: ")
        ));
    }

    /**
     * OWNER: Huỳnh Duy Tâm
     * USE CASE: UC-3 - Đăng nhập
     * ACTOR: Guest
     * FLOW: Basic Flow / Exception Flow
     * PURPOSE: Nhập username/email và password rồi gọi AuthController để set currentUser nếu hợp lệ.
     * SEQUENCE NOTE: ConsoleView -> AuthController -> AuthService -> UserRepository/ActivityLogRepository -> SessionManager.
     */
    public void showLoginForm() {
        System.out.println("\n--- UC-3 Đăng nhập ---");
        printResult(authController.login(prompt("Username/email: "), prompt("Password: ")));
    }

    /**
     * OWNER: Huỳnh Duy Tâm
     * USE CASE: UC-3 - Đăng xuất
     * ACTOR: User/Moderator/Admin
     * FLOW: Basic Flow
     * PURPOSE: Gọi AuthController để clear currentUser.
     * SEQUENCE NOTE: ConsoleView -> AuthController -> AuthService -> UserRepository/ActivityLogRepository -> SessionManager.
     */
    public void showLogoutAction() {
        printResult(authController.logout());
    }

    private String prompt(String label) {
        System.out.print(label);
        return scanner.nextLine();
    }

    private void printResult(OperationResult<?> result) {
        System.out.println((result.isSuccess() ? "[OK] " : "[FAIL] ") + result.getMessage());
        if (result.getData() != null) {
            System.out.println(result.getData());
        }
    }
}
