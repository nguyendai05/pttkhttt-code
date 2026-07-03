package service;

import model.OtpRequest;
import model.UserAccount;
import repository.UserRepository;
import util.InputValidator;
import util.OperationResult;
import util.PasswordUtil;


public class PasswordRecoveryService {
    private UserRepository userRepository;
    private OtpService otpService;

    public PasswordRecoveryService(UserRepository userRepository, OtpService otpService) {
        this.userRepository = userRepository;
        this.otpService = otpService;
    }



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
