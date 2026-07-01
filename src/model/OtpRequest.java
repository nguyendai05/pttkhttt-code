package model;

import java.time.LocalDateTime;

/**
 * OWNER: Team 17
 * FEATURE GROUP: Model entity dùng chung
 * PURPOSE: Thành phần nền tảng của phần 1, phục vụ mô phỏng dữ liệu và luồng nghiệp vụ ở các phần sau.
 */
public class OtpRequest {
    public String email;
    public String otpCode;
    public LocalDateTime expiredAt;
    public boolean used;

    public OtpRequest(String email, String otpCode, LocalDateTime expiredAt) {
        this.email = email;
        this.otpCode = otpCode;
        this.expiredAt = expiredAt;
    }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expiredAt);
    }
}
