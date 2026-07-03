package controller;

import model.UserAccount;
import service.RegistrationService;
import util.OperationResult;

/**
 * OWNER: Nguyễn Xuân Đại
 * FEATURE GROUP: Đăng ký tài khoản
 * RELATED USE CASES: UC-1
 * PURPOSE: Nhận request đăng ký từ ConsoleView và chuyển cho RegistrationService.
 */
public class RegistrationController {
    private RegistrationService registrationService;

    public RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    /**
     * OWNER: Nguyễn Xuân Đại
     * USE CASE: UC-1 - Đăng ký
     * ACTOR: Guest
     * FLOW: Basic Flow / Exception Flow
     * PURPOSE: Điều phối dữ liệu đăng ký từ View sang Service.
     * SEQUENCE NOTE: ConsoleView -> RegistrationController -> RegistrationService -> UserRepository/ProfileRepository -> SessionManager.
     */
    public OperationResult<UserAccount> register(String username, String email, String password, String confirmPassword) {
        return registrationService.register(username, email, password, confirmPassword);
    }
}
