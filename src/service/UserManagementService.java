package service;

import model.UserAccount;
import model.enums.AccountStatus;
import model.enums.Role;
import repository.UserRepository;
import util.OperationResult;

import java.util.List;

/**
 * OWNER: Nguyễn Minh Luân
 * FEATURE GROUP: Quản trị, phân quyền người dùng
 * RELATED USE CASES: UC-4
 * PURPOSE: Xử lý xem/tìm user, đổi role, khóa/mở khóa và các ràng buộc phân quyền.
 */
public class UserManagementService {
    private final UserRepository userRepository;
    private final SessionManager sessionManager;
    private final ActivityLogService activityLogService;

    public UserManagementService(UserRepository userRepository, SessionManager sessionManager, ActivityLogService activityLogService) {
        this.userRepository = userRepository;
        this.sessionManager = sessionManager;
        this.activityLogService = activityLogService;
    }

    /**
     * OWNER: Nguyễn Minh Luân
     * USE CASE: UC-4 - Xem/tìm kiếm người dùng
     * ACTOR: Admin/Moderator
     * FLOW: Basic Flow / Alternative Flow
     * PURPOSE: Admin và Moderator được xem/tìm kiếm user theo keyword, role, status.
     * SEQUENCE NOTE: ConsoleView -> UserManagementController -> UserManagementService -> UserRepository -> SessionManager.
     */
    public OperationResult<List<UserAccount>> searchUsers(String keyword, Role role, AccountStatus status) {
        UserAccount actor = sessionManager.getCurrentUser().orElse(null);
        if (actor == null || (actor.role != Role.ADMIN && actor.role != Role.MODERATOR)) {
            return OperationResult.fail("Chỉ Admin hoặc Moderator được xem/tìm kiếm user.");
        }
        return OperationResult.ok("Danh sách user phù hợp.", userRepository.search(keyword, role, status));
    }

    /**
     * OWNER: Nguyễn Minh Luân
     * USE CASE: UC-4 - Đổi role người dùng
     * ACTOR: Admin
     * FLOW: Basic Flow / Exception Flow
     * PURPOSE: Admin đổi role USER/MODERATOR, không để hệ thống mất toàn bộ Admin ACTIVE.
     * SEQUENCE NOTE: ConsoleView -> UserManagementController -> UserManagementService -> UserRepository/ActivityLogRepository -> SessionManager.
     */
    public OperationResult<UserAccount> changeRole(String userId, Role newRole) {
        UserAccount admin = requireAdmin();
        if (admin == null) {
            return OperationResult.fail("Chỉ Admin được đổi role.");
        }
        if (newRole != Role.USER && newRole != Role.MODERATOR) {
            return OperationResult.fail("Role mới chỉ được là USER hoặc MODERATOR.");
        }
        UserAccount target = userRepository.findById(userId).orElse(null);
        if (target == null) {
            return OperationResult.fail("Không tìm thấy user.");
        }
        if (target.role == Role.ADMIN && target.status == AccountStatus.ACTIVE && userRepository.countActiveAdmins() <= 1) {
            return OperationResult.fail("Không được đổi role Admin ACTIVE cuối cùng.");
        }
        Role oldRole = target.role;
        target.role = newRole;
        userRepository.save(target);
        activityLogService.log(admin.id, "CHANGE_ROLE", "USER", target.id, oldRole + " -> " + newRole);
        return OperationResult.ok("Đổi role thành công.", target);
    }

    /**
     * OWNER: Nguyễn Minh Luân
     * USE CASE: UC-4 - Khóa/mở khóa tài khoản
     * ACTOR: Admin
     * FLOW: Basic Flow / Exception Flow
     * PURPOSE: Admin đổi trạng thái ACTIVE/LOCKED, không tự khóa mình và không khóa Admin ACTIVE cuối cùng.
     * SEQUENCE NOTE: ConsoleView -> UserManagementController -> UserManagementService -> UserRepository/ActivityLogRepository -> SessionManager.
     */
    public OperationResult<UserAccount> changeStatus(String userId, AccountStatus newStatus) {
        UserAccount admin = requireAdmin();
        if (admin == null) {
            return OperationResult.fail("Chỉ Admin được khóa/mở khóa user.");
        }
        UserAccount target = userRepository.findById(userId).orElse(null);
        if (target == null) {
            return OperationResult.fail("Không tìm thấy user.");
        }
        if (target.id.equals(admin.id) && newStatus == AccountStatus.LOCKED) {
            return OperationResult.fail("Admin không được tự khóa chính mình.");
        }
        if (target.role == Role.ADMIN && target.status == AccountStatus.ACTIVE
                && newStatus == AccountStatus.LOCKED && userRepository.countActiveAdmins() <= 1) {
            return OperationResult.fail("Không được khóa Admin ACTIVE cuối cùng.");
        }
        AccountStatus oldStatus = target.status;
        target.status = newStatus;
        userRepository.save(target);
        activityLogService.log(admin.id, "CHANGE_STATUS", "USER", target.id, oldStatus + " -> " + newStatus);
        return OperationResult.ok("Đổi trạng thái thành công.", target);
    }

    private UserAccount requireAdmin() {
        UserAccount actor = sessionManager.getCurrentUser().orElse(null);
        return actor != null && actor.role == Role.ADMIN ? actor : null;
    }
}
