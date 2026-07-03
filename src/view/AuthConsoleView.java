package view;

import controller.AuthController;
import controller.PasswordRecoveryController;
import controller.RegistrationController;
import model.OtpRequest;
import util.OperationResult;

import java.util.Scanner;


public class AuthConsoleView {
    private RegistrationController registrationController;
    private AuthController authController;
    private PasswordRecoveryController passwordRecoveryController;
    private Scanner scanner;

    public AuthConsoleView(RegistrationController registrationController,
                           AuthController authController,
                           PasswordRecoveryController passwordRecoveryController) {
        this.registrationController = registrationController;
        this.authController = authController;
        this.passwordRecoveryController = passwordRecoveryController;
        this.scanner = new Scanner(System.in, "UTF-8");
    }



    public void showRegisterForm() {
        System.out.println("\n--- UC-1 Đăng ký ---");
        printResult(registrationController.register(
                prompt("Username: "),
                prompt("Email: "),
                prompt("Password: "),
                prompt("Confirm password: ")
        ));
    }



    public void showPasswordRecoveryForm() {
        System.out.println("\n--- UC-2 Khôi phục mật khẩu bằng OTP ---");
        String email = prompt("Email: ");
        OperationResult<OtpRequest> otpResult = passwordRecoveryController.requestOtp(email);
        printResult(otpResult);
        if (!otpResult.isSuccess()) {
            return;
        }
        System.out.println("OTP mô phỏng gửi email: " + otpResult.getData().getOtpCode());
        printResult(passwordRecoveryController.resetPassword(
                email,
                prompt("OTP: "),
                prompt("Password mới: "),
                prompt("Confirm password mới: ")
        ));
    }



    public void showLoginForm() {
        System.out.println("\n--- UC-3 Đăng nhập ---");
        printResult(authController.login(prompt("Username/email: "), prompt("Password: ")));
    }



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
