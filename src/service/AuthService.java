package service;

import model.ActivityLog;
import model.UserAccount;
import model.enums.AccountStatus;
import repository.ActivityLogRepository;
import repository.UserRepository;
import util.IdGenerator;
import util.InputValidator;
import util.OperationResult;
import util.PasswordUtil;

import java.time.LocalDateTime;

/**
 * OWNER: Huỳnh Duy Tâm
 * FEATURE GROUP: Đăng nhập / đăng xuất
 * RELATED USE CASES: UC-3
 * PURPOSE: Xác thực username/email, password, trạng thái tài khoản và cập nhật session.
 */
public class AuthService {
    private UserRepository userRepository;
    private ActivityLogRepository activityLogRepository;
    private SessionManager sessionManager;

    public AuthService(UserRepository userRepository, ActivityLogRepository activityLogRepository, SessionManager sessionManager) {
        this.userRepository = userRepository;
        this.activityLogRepository = activityLogRepository;
        this.sessionManager = sessionManager;
    }

    /**
     * OWNER: Huỳnh Duy Tâm
     * USE CASE: UC-3 - Đăng nhập
     * ACTOR: Guest
     * FLOW: Basic Flow / Exception Flow
     * PURPOSE: Đăng nhập bằng username/email, kiểm tra password hash và tài khoản ACTIVE, sau đó set currentUser.
     * SEQUENCE NOTE: ConsoleView -> AuthController -> AuthService -> UserRepository/ActivityLogRepository -> SessionManager.
     */
    public OperationResult<UserAccount> login(String identity, String password) {
        if (InputValidator.isBlank(identity) || InputValidator.isBlank(password)) {
            return OperationResult.fail("Vui lòng nhập username/email và password.");
        }
        UserAccount user = userRepository.findByUsernameOrEmail(identity.trim()).orElse(null);
        if (user == null) {
            return OperationResult.fail("Tài khoản không tồn tại.");
        }
        if (!PasswordUtil.matches(password, user.getPasswordHash())) {
            return OperationResult.fail("Password không đúng.");
        }
        if (user.getStatus() != AccountStatus.ACTIVE) {
            return OperationResult.fail("Tài khoản không ở trạng thái ACTIVE.");
        }

        user.setLastLoginAt(LocalDateTime.now());
        userRepository.save(user);
        sessionManager.login(user);
        activityLogRepository.save(new ActivityLog(
                IdGenerator.nextId("LOG"),
                user.getId(),
                "LOGIN",
                "USER",
                user.getId(),
                "Đăng nhập thành công bằng username/email"
        ));
        return OperationResult.ok("Đăng nhập thành công: " + user.getUsername() + " (" + user.getRole() + ").", user);
    }

    /**
     * OWNER: Huỳnh Duy Tâm
     * USE CASE: UC-3 - Đăng xuất
     * ACTOR: User/Moderator/Admin
     * FLOW: Basic Flow
     * PURPOSE: Clear currentUser khỏi SessionManager.
     * SEQUENCE NOTE: ConsoleView -> AuthController -> AuthService -> UserRepository/ActivityLogRepository -> SessionManager.
     */
    public OperationResult<Void> logout() {
        if (!sessionManager.isLoggedIn()) {
            return OperationResult.fail("Chưa có người dùng đăng nhập.");
        }
        sessionManager.logout();
        return OperationResult.ok("Đăng xuất thành công.", null);
    }
}
