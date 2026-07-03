package controller;

import model.OtpRequest;
import service.PasswordRecoveryService;
import util.OperationResult;

/**
 * OWNER: Huỳnh Duy Tâm
 * FEATURE GROUP: Khôi phục mật khẩu bằng OTP
 * RELATED USE CASES: UC-2
 * PURPOSE: Nhận request quên mật khẩu từ ConsoleView và gọi PasswordRecoveryService.
 */
public class PasswordRecoveryController {
    private PasswordRecoveryService passwordRecoveryService;

    public PasswordRecoveryController(PasswordRecoveryService passwordRecoveryService) {
        this.passwordRecoveryService = passwordRecoveryService;
    }

    /**
     * OWNER: Huỳnh Duy Tâm
     * USE CASE: UC-2 - Yêu cầu OTP
     * ACTOR: Guest
     * FLOW: Basic Flow / Exception Flow
     * PURPOSE: Điều phối yêu cầu sinh OTP theo email.
     * SEQUENCE NOTE: ConsoleView -> PasswordRecoveryController -> PasswordRecoveryService -> UserRepository/OtpRepository -> SessionManager.
     */
    public OperationResult<OtpRequest> requestOtp(String email) {
        return passwordRecoveryService.requestOtp(email);
    }

    /**
     * OWNER: Huỳnh Duy Tâm
     * USE CASE: UC-2 - Đặt lại mật khẩu
     * ACTOR: Guest
     * FLOW: Basic Flow / Exception Flow
     * PURPOSE: Điều phối xác thực OTP và cập nhật passwordHash.
     * SEQUENCE NOTE: ConsoleView -> PasswordRecoveryController -> PasswordRecoveryService -> UserRepository/OtpRepository -> SessionManager.
     */
    public OperationResult<Void> resetPassword(String email, String otpCode, String newPassword, String confirmPassword) {
        return passwordRecoveryService.resetPassword(email, otpCode, newPassword, confirmPassword);
    }
}
