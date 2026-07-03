package service;

import model.UserAccount;
import model.enums.AccountStatus;
import model.enums.Role;
import repository.UserRepository;
import util.OperationResult;

import java.util.List;

/**
 * OWNER: Nguyá»…n Minh LuĂ¢n
 * FEATURE GROUP: Quáº£n trá»‹, phĂ¢n quyá»n ngÆ°á»i dĂ¹ng
 * RELATED USE CASES: UC-4
 * PURPOSE: Xá»­ lĂ½ xem/tĂ¬m user, Ä‘á»•i role, khĂ³a/má»Ÿ khĂ³a vĂ  cĂ¡c rĂ ng buá»™c phĂ¢n quyá»n.
 */
public class UserManagementService {
    private UserRepository userRepository;
    private SessionManager sessionManager;
    private ActivityLogService activityLogService;

    public UserManagementService(UserRepository userRepository, SessionManager sessionManager, ActivityLogService activityLogService) {
        this.userRepository = userRepository;
        this.sessionManager = sessionManager;
        this.activityLogService = activityLogService;
    }

    /**
     * OWNER: Nguyá»…n Minh LuĂ¢n
     * USE CASE: UC-4 - Xem/tĂ¬m kiáº¿m ngÆ°á»i dĂ¹ng
     * ACTOR: Admin/Moderator
     * FLOW: Basic Flow / Alternative Flow
     * PURPOSE: Admin vĂ  Moderator Ä‘Æ°á»£c xem/tĂ¬m kiáº¿m user theo keyword, role, status.
     * SEQUENCE NOTE: ConsoleView -> UserManagementController -> UserManagementService -> UserRepository -> SessionManager.
     */
    public OperationResult<List<UserAccount>> searchUsers(String keyword, Role role, AccountStatus status) {
        UserAccount actor = sessionManager.getCurrentUser().orElse(null);
        if (actor == null || (actor.getRole() != Role.ADMIN && actor.getRole() != Role.MODERATOR)) {
            return OperationResult.fail("Chá»‰ Admin hoáº·c Moderator Ä‘Æ°á»£c xem/tĂ¬m kiáº¿m user.");
        }
        return OperationResult.ok("Danh sĂ¡ch user phĂ¹ há»£p.", userRepository.search(keyword, role, status));
    }

    /**
     * OWNER: Nguyá»…n Minh LuĂ¢n
     * USE CASE: UC-4 - Äá»•i role ngÆ°á»i dĂ¹ng
     * ACTOR: Admin
     * FLOW: Basic Flow / Exception Flow
     * PURPOSE: Admin Ä‘á»•i role USER/MODERATOR, khĂ´ng Ä‘á»ƒ há»‡ thá»‘ng máº¥t toĂ n bá»™ Admin ACTIVE.
     * SEQUENCE NOTE: ConsoleView -> UserManagementController -> UserManagementService -> UserRepository/ActivityLogRepository -> SessionManager.
     */
    public OperationResult<UserAccount> changeRole(String userId, Role newRole) {
        UserAccount admin = requireAdmin();
        if (admin == null) {
            return OperationResult.fail("Chá»‰ Admin Ä‘Æ°á»£c Ä‘á»•i role.");
        }
        if (newRole != Role.USER && newRole != Role.MODERATOR) {
            return OperationResult.fail("Role má»›i chá»‰ Ä‘Æ°á»£c lĂ  USER hoáº·c MODERATOR.");
        }
        UserAccount target = userRepository.findById(userId).orElse(null);
        if (target == null) {
            return OperationResult.fail("KhĂ´ng tĂ¬m tháº¥y user.");
        }
        if (target.getRole() == Role.ADMIN && target.getStatus() == AccountStatus.ACTIVE && userRepository.countActiveAdmins() <= 1) {
            return OperationResult.fail("KhĂ´ng Ä‘Æ°á»£c Ä‘á»•i role Admin ACTIVE cuá»‘i cĂ¹ng.");
        }
        Role oldRole = target.getRole();
        target.setRole(newRole);
        userRepository.save(target);
        activityLogService.log(admin.getId(), "CHANGE_ROLE", "USER", target.getId(), oldRole + " -> " + newRole);
        return OperationResult.ok("Äá»•i role thĂ nh cĂ´ng.", target);
    }

    /**
     * OWNER: Nguyá»…n Minh LuĂ¢n
     * USE CASE: UC-4 - KhĂ³a/má»Ÿ khĂ³a tĂ i khoáº£n
     * ACTOR: Admin
     * FLOW: Basic Flow / Exception Flow
     * PURPOSE: Admin Ä‘á»•i tráº¡ng thĂ¡i ACTIVE/LOCKED, khĂ´ng tá»± khĂ³a mĂ¬nh vĂ  khĂ´ng khĂ³a Admin ACTIVE cuá»‘i cĂ¹ng.
     * SEQUENCE NOTE: ConsoleView -> UserManagementController -> UserManagementService -> UserRepository/ActivityLogRepository -> SessionManager.
     */
    public OperationResult<UserAccount> changeStatus(String userId, AccountStatus newStatus) {
        UserAccount admin = requireAdmin();
        if (admin == null) {
            return OperationResult.fail("Chá»‰ Admin Ä‘Æ°á»£c khĂ³a/má»Ÿ khĂ³a user.");
        }
        UserAccount target = userRepository.findById(userId).orElse(null);
        if (target == null) {
            return OperationResult.fail("KhĂ´ng tĂ¬m tháº¥y user.");
        }
        if (target.getId().equals(admin.getId()) && newStatus == AccountStatus.LOCKED) {
            return OperationResult.fail("Admin khĂ´ng Ä‘Æ°á»£c tá»± khĂ³a chĂ­nh mĂ¬nh.");
        }
        if (target.getRole() == Role.ADMIN && target.getStatus() == AccountStatus.ACTIVE
                && newStatus == AccountStatus.LOCKED && userRepository.countActiveAdmins() <= 1) {
            return OperationResult.fail("KhĂ´ng Ä‘Æ°á»£c khĂ³a Admin ACTIVE cuá»‘i cĂ¹ng.");
        }
        AccountStatus oldStatus = target.getStatus();
        target.setStatus(newStatus);
        userRepository.save(target);
        activityLogService.log(admin.getId(), "CHANGE_STATUS", "USER", target.getId(), oldStatus + " -> " + newStatus);
        return OperationResult.ok("Äá»•i tráº¡ng thĂ¡i thĂ nh cĂ´ng.", target);
    }

    private UserAccount requireAdmin() {
        UserAccount actor = sessionManager.getCurrentUser().orElse(null);
        return actor != null && actor.getRole() == Role.ADMIN ? actor : null;
    }
}
