package service;

import model.OtpRequest;
import repository.OtpRepository;
import util.OtpGenerator;

import java.time.LocalDateTime;
import java.util.Optional;


public class OtpService {
    private OtpRepository otpRepository;

    public OtpService(OtpRepository otpRepository) {
        this.otpRepository = otpRepository;
    }



    public OtpRequest generateOtp(String email) {
        OtpRequest otpRequest = new OtpRequest(email, OtpGenerator.generate(), LocalDateTime.now().plusMinutes(5));
        otpRepository.save(otpRequest);
        return otpRequest;
    }

    public Optional<OtpRequest> findByEmail(String email) {
        return otpRepository.findByEmail(email);
    }
}
