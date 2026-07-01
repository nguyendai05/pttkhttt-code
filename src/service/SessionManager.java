package service;

import model.UserAccount;

import java.util.Optional;

/**
 * OWNER: Huỳnh Duy Tâm
 * FEATURE GROUP: Auth + session
 * RELATED USE CASES: UC-3
 * PURPOSE: Lưu currentUser trong phiên chạy console để mô phỏng đăng nhập/đăng xuất.
 */
public class SessionManager {
    private UserAccount currentUser;

    /**
     * OWNER: Huỳnh Duy Tâm
     * USE CASE: UC-3 - Đăng nhập
     * ACTOR: Guest
     * FLOW: Basic Flow
     * PURPOSE: Lưu user đăng nhập thành công thành currentUser của phiên console.
     * SEQUENCE NOTE: ConsoleView -> AuthController -> AuthService -> UserRepository/ActivityLogRepository -> SessionManager.
     */
    public void login(UserAccount user) {
        this.currentUser = user;
    }

    /**
     * OWNER: Huỳnh Duy Tâm
     * USE CASE: UC-3 - Đăng xuất
     * ACTOR: User/Moderator/Admin
     * FLOW: Basic Flow
     * PURPOSE: Xóa currentUser để kết thúc phiên đăng nhập hiện tại.
     * SEQUENCE NOTE: ConsoleView -> AuthController -> AuthService -> UserRepository/ActivityLogRepository -> SessionManager.
     */
    public void logout() {
        this.currentUser = null;
    }

    public Optional<UserAccount> getCurrentUser() {
        return Optional.ofNullable(currentUser);
    }

    public boolean isLoggedIn() {
        return currentUser != null;
    }
}