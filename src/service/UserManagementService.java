package service;

import model.UserAccount;
import model.enums.AccountStatus;
import model.enums.Role;
import repository.UserRepository;
import util.OperationResult;

import java.util.List;


public class UserManagementService {
    private UserRepository userRepository;
    private SessionManager sessionManager;
    private ActivityLogService activityLogService;

    public UserManagementService(UserRepository userRepository, SessionManager sessionManager, ActivityLogService activityLogService) {
        this.userRepository = userRepository;
        this.sessionManager = sessionManager;
        this.activityLogService = activityLogService;
    }



    public OperationResult<List<UserAccount>> searchUsers(String keyword, Role role, AccountStatus status) {
        UserAccount actor = sessionManager.getCurrentUser().orElse(null);
        if (actor == null || (actor.getRole() != Role.ADMIN && actor.getRole() != Role.MODERATOR)) {
            return OperationResult.fail("Chỉ Admin hoặc Moderator được xem/tìm kiếm user.");
        }
        return OperationResult.ok("Danh sách user phù hợp.", userRepository.search(keyword, role, status));
    }



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
        if (target.getRole() == Role.ADMIN && target.getStatus() == AccountStatus.ACTIVE && userRepository.countActiveAdmins() <= 1) {
            return OperationResult.fail("Không được đổi role Admin ACTIVE cuối cùng.");
        }
        Role oldRole = target.getRole();
        target.setRole(newRole);
        userRepository.save(target);
        activityLogService.log(admin.getId(), "CHANGE_ROLE", "USER", target.getId(), oldRole + " -> " + newRole);
        return OperationResult.ok("Đổi role thành công.", target);
    }



    public OperationResult<UserAccount> changeStatus(String userId, AccountStatus newStatus) {
        UserAccount admin = requireAdmin();
        if (admin == null) {
            return OperationResult.fail("Chỉ Admin được khóa/mở khóa user.");
        }
        UserAccount target = userRepository.findById(userId).orElse(null);
        if (target == null) {
            return OperationResult.fail("Không tìm thấy user.");
        }
        if (target.getId().equals(admin.getId()) && newStatus == AccountStatus.LOCKED) {
            return OperationResult.fail("Admin không được tự khóa chính mình.");
        }
        if (target.getRole() == Role.ADMIN && target.getStatus() == AccountStatus.ACTIVE
                && newStatus == AccountStatus.LOCKED && userRepository.countActiveAdmins() <= 1) {
            return OperationResult.fail("Không được khóa Admin ACTIVE cuối cùng.");
        }
        AccountStatus oldStatus = target.getStatus();
        target.setStatus(newStatus);
        userRepository.save(target);
        activityLogService.log(admin.getId(), "CHANGE_STATUS", "USER", target.getId(), oldStatus + " -> " + newStatus);
        return OperationResult.ok("Đổi trạng thái thành công.", target);
    }

    private UserAccount requireAdmin() {
        UserAccount actor = sessionManager.getCurrentUser().orElse(null);
        return actor != null && actor.getRole() == Role.ADMIN ? actor : null;
    }
}
