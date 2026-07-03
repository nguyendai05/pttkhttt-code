package controller;

import model.OtpRequest;
import service.PasswordRecoveryService;
import util.OperationResult;


public class PasswordRecoveryController {
    private PasswordRecoveryService passwordRecoveryService;

    public PasswordRecoveryController(PasswordRecoveryService passwordRecoveryService) {
        this.passwordRecoveryService = passwordRecoveryService;
    }



    public OperationResult<OtpRequest> requestOtp(String email) {
        return passwordRecoveryService.requestOtp(email);
    }



    public OperationResult<Void> resetPassword(String email, String otpCode, String newPassword, String confirmPassword) {
        return passwordRecoveryService.resetPassword(email, otpCode, newPassword, confirmPassword);
    }
}
