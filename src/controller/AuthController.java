package controller;

import model.UserAccount;
import service.AuthService;
import util.OperationResult;

/**
 * OWNER: Huỳnh Duy Tâm
 * FEATURE GROUP: Đăng nhập / đăng xuất
 * RELATED USE CASES: UC-3
 * PURPOSE: Nhận request login/logout từ ConsoleView và gọi AuthService.
 */
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * OWNER: Huỳnh Duy Tâm
     * USE CASE: UC-3 - Đăng nhập
     * ACTOR: Guest
     * FLOW: Basic Flow / Exception Flow
     * PURPOSE: Điều phối đăng nhập bằng username/email và password.
     * SEQUENCE NOTE: ConsoleView -> AuthController -> AuthService -> UserRepository/ActivityLogRepository -> SessionManager.
     */
    public OperationResult<UserAccount> login(String identity, String password) {
        return authService.login(identity, password);
    }

    /**
     * OWNER: Huỳnh Duy Tâm
     * USE CASE: UC-3 - Đăng xuất
     * ACTOR: User/Moderator/Admin
     * FLOW: Basic Flow
     * PURPOSE: Điều phối đăng xuất và clear currentUser.
     * SEQUENCE NOTE: ConsoleView -> AuthController -> AuthService -> UserRepository/ActivityLogRepository -> SessionManager.
     */
    public OperationResult<Void> logout() {
        return authService.logout();
    }
}
