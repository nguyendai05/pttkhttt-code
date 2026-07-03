package repository;

import model.OtpRequest;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


public class OtpRepository {
    private Map<String, OtpRequest> otpByEmail = new HashMap<>();

    public void save(OtpRequest otpRequest) {
        otpByEmail.put(otpRequest.getEmail().toLowerCase(), otpRequest);
    }

    public Optional<OtpRequest> findByEmail(String email) {
        return Optional.ofNullable(otpByEmail.get(email.toLowerCase()));
    }
}
