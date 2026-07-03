package service;

import model.OtpRequest;
import model.UserAccount;
import repository.UserRepository;
import util.InputValidator;
import util.OperationResult;
import util.PasswordUtil;

/**
 * OWNER: Huỳnh Duy Tâm
 * FEATURE GROUP: Khôi phục mật khẩu bằng OTP
 * RELATED USE CASES: UC-2
 * PURPOSE: Điều phối yêu cầu OTP và đặt lại mật khẩu khi OTP hợp lệ.
 */
public class PasswordRecoveryService {
    private UserRepository userRepository;
    private OtpService otpService;

    public PasswordRecoveryService(UserRepository userRepository, OtpService otpService) {
        this.userRepository = userRepository;
        this.otpService = otpService;
    }

    /**
     * OWNER: Huỳnh Duy Tâm
     * USE CASE: UC-2 - Yêu cầu OTP
     * ACTOR: Guest
     * FLOW: Basic Flow / Exception Flow
     * PURPOSE: Kiểm tra email tồn tại, sinh OTP và trả OTP để View in ra console mô phỏng gửi email.
     * SEQUENCE NOTE: ConsoleView -> PasswordRecoveryController -> PasswordRecoveryService -> UserRepository/OtpRepository -> SessionManager.
     */
    public OperationResult<OtpRequest> requestOtp(String email) {
        if (InputValidator.isBlank(email) || !InputValidator.isValidEmail(email)) {
            return OperationResult.fail("Email không hợp lệ.");
        }
        UserAccount user = userRepository.findByUsernameOrEmail(email.trim()).orElse(null);
        if (user == null || !user.getEmail().equalsIgnoreCase(email.trim())) {
            return OperationResult.fail("Email chưa được đăng ký trong hệ thống.");
        }
        OtpRequest otpRequest = otpService.generateOtp(email.trim());
        return OperationResult.ok("OTP đã được sinh. View sẽ in OTP ra console để mô phỏng gửi email.", otpRequest);
    }

    /**
     * OWNER: Huỳnh Duy Tâm
     * USE CASE: UC-2 - Đặt lại mật khẩu
     * ACTOR: Guest
     * FLOW: Basic Flow / Exception Flow
     * PURPOSE: Kiểm tra OTP đúng, chưa hết hạn, chưa dùng và cập nhật passwordHash mới.
     * SEQUENCE NOTE: ConsoleView -> PasswordRecoveryController -> PasswordRecoveryService -> UserRepository/OtpRepository -> SessionManager.
     */
    public OperationResult<Void> resetPassword(String email, String otpCode, String newPassword, String confirmPassword) {
        if (!InputValidator.isStrongPassword(newPassword)) {
            return OperationResult.fail("Password mới phải có tối thiểu 8 ký tự.");
        }
        if (!newPassword.equals(confirmPassword)) {
            return OperationResult.fail("Password mới và confirmPassword không khớp.");
        }

        OtpRequest otpRequest = otpService.findByEmail(email.trim()).orElse(null);
        if (otpRequest == null) {
            return OperationResult.fail("Chưa có OTP cho email này.");
        }
        if (otpRequest.isUsed()) {
            return OperationResult.fail("OTP đã được sử dụng.");
        }
        if (otpRequest.isExpired()) {
            return OperationResult.fail("OTP đã hết hạn.");
        }
        if (!otpRequest.getOtpCode().equals(otpCode)) {
            return OperationResult.fail("OTP không đúng.");
        }

        UserAccount user = userRepository.findByUsernameOrEmail(email.trim()).orElse(null);
        if (user == null) {
            return OperationResult.fail("Không tìm thấy tài khoản cần cập nhật mật khẩu.");
        }
        user.setPasswordHash(PasswordUtil.hash(newPassword));
        userRepository.save(user);
        otpRequest.setUsed(true);
        return OperationResult.ok("Cập nhật mật khẩu thành công.", null);
    }
}
