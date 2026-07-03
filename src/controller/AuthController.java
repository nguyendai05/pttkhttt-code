package controller;

import model.UserAccount;
import service.AuthService;
import util.OperationResult;


public class AuthController {
    private AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }



    public OperationResult<UserAccount> login(String identity, String password) {
        return authService.login(identity, password);
    }



    public OperationResult<Void> logout() {
        return authService.logout();
    }
}
