package repository;

import model.OtpRequest;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * OWNER: Team 17
 * FEATURE GROUP: Repository in-memory dùng chung
 * PURPOSE: Thành phần nền tảng của phần 1, phục vụ mô phỏng dữ liệu và luồng nghiệp vụ ở các phần sau.
 */
public class OtpRepository {
    private final Map<String, OtpRequest> otpByEmail = new HashMap<>();

    public void save(OtpRequest otpRequest) {
        otpByEmail.put(otpRequest.email.toLowerCase(), otpRequest);
    }

    public Optional<OtpRequest> findByEmail(String email) {
        return Optional.ofNullable(otpByEmail.get(email.toLowerCase()));
    }
}
