package controller;

import model.UserAccount;
import model.enums.AccountStatus;
import model.enums.Role;
import service.UserManagementService;
import util.OperationResult;

import java.util.List;

/**
 * OWNER: Nguyễn Minh Luân
 * FEATURE GROUP: Quản trị, phân quyền người dùng
 * RELATED USE CASES: UC-4
 * PURPOSE: Nhận request quản trị user từ ConsoleView và gọi UserManagementService.
 */
public class UserManagementController {
    private UserManagementService userManagementService;

    public UserManagementController(UserManagementService userManagementService) {
        this.userManagementService = userManagementService;
    }

    /**
     * OWNER: Nguyễn Minh Luân
     * USE CASE: UC-4 - Xem/tìm kiếm người dùng
     * ACTOR: Admin/Moderator
     * FLOW: Basic Flow / Alternative Flow
     * PURPOSE: Điều phối xem/tìm kiếm user theo keyword, role, status.
     * SEQUENCE NOTE: ConsoleView -> UserManagementController -> UserManagementService -> UserRepository -> SessionManager.
     */
    public OperationResult<List<UserAccount>> searchUsers(String keyword, Role role, AccountStatus status) {
        return userManagementService.searchUsers(keyword, role, status);
    }

    /**
     * OWNER: Nguyễn Minh Luân
     * USE CASE: UC-4 - Đổi role người dùng
     * ACTOR: Admin
     * FLOW: Basic Flow / Exception Flow
     * PURPOSE: Điều phối Admin đổi role USER/MODERATOR.
     * SEQUENCE NOTE: ConsoleView -> UserManagementController -> UserManagementService -> UserRepository/ActivityLogRepository -> SessionManager.
     */
    public OperationResult<UserAccount> changeRole(String userId, Role newRole) {
        return userManagementService.changeRole(userId, newRole);
    }

    /**
     * OWNER: Nguyễn Minh Luân
     * USE CASE: UC-4 - Khóa/mở khóa tài khoản
     * ACTOR: Admin
     * FLOW: Basic Flow / Exception Flow
     * PURPOSE: Điều phối Admin đổi trạng thái ACTIVE/LOCKED.
     * SEQUENCE NOTE: ConsoleView -> UserManagementController -> UserManagementService -> UserRepository/ActivityLogRepository -> SessionManager.
     */
    public OperationResult<UserAccount> changeStatus(String userId, AccountStatus newStatus) {
        return userManagementService.changeStatus(userId, newStatus);
    }
}
