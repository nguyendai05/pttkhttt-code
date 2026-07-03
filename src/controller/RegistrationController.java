package controller;

import model.UserAccount;
import service.RegistrationService;
import util.OperationResult;


public class RegistrationController {
    private RegistrationService registrationService;

    public RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }



    public OperationResult<UserAccount> register(String username, String email, String password, String confirmPassword) {
        return registrationService.register(username, email, password, confirmPassword);
    }
}
