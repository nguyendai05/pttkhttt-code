package service;

import model.OtpRequest;
import repository.OtpRepository;
import util.OtpGenerator;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * OWNER: Huỳnh Duy Tâm
 * FEATURE GROUP: Khôi phục mật khẩu bằng OTP
 * RELATED USE CASES: UC-2
 * PURPOSE: Sinh, lưu và truy xuất OTP phục vụ reset mật khẩu.
 */
public class OtpService {
    private OtpRepository otpRepository;

    public OtpService(OtpRepository otpRepository) {
        this.otpRepository = otpRepository;
    }

    /**
     * OWNER: Huỳnh Duy Tâm
     * USE CASE: UC-2 - Sinh OTP
     * ACTOR: Guest
     * FLOW: Basic Flow
     * PURPOSE: Sinh OTP có hạn 5 phút và lưu vào OtpRepository.
     * SEQUENCE NOTE: ConsoleView -> PasswordRecoveryController -> PasswordRecoveryService -> OtpRepository -> SessionManager.
     */
    public OtpRequest generateOtp(String email) {
        OtpRequest otpRequest = new OtpRequest(email, OtpGenerator.generate(), LocalDateTime.now().plusMinutes(5));
        otpRepository.save(otpRequest);
        return otpRequest;
    }

    public Optional<OtpRequest> findByEmail(String email) {
        return otpRepository.findByEmail(email);
    }
}
