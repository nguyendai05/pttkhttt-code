package model;

import java.time.LocalDateTime;


public class OtpRequest {
    private String email;
    private String otpCode;
    private LocalDateTime expiredAt;
    private boolean used;

    public OtpRequest(String email, String otpCode, LocalDateTime expiredAt) {
        this.email = email;
        this.otpCode = otpCode;
        this.expiredAt = expiredAt;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOtpCode() {
        return otpCode;
    }

    public void setOtpCode(String otpCode) {
        this.otpCode = otpCode;
    }

    public LocalDateTime getExpiredAt() {
        return expiredAt;
    }

    public void setExpiredAt(LocalDateTime expiredAt) {
        this.expiredAt = expiredAt;
    }

    public boolean isUsed() {
        return used;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expiredAt);
    }
}
