package controller;

import model.UserAccount;
import model.enums.AccountStatus;
import model.enums.Role;
import service.UserManagementService;
import util.OperationResult;

import java.util.List;


public class UserManagementController {
    private UserManagementService userManagementService;

    public UserManagementController(UserManagementService userManagementService) {
        this.userManagementService = userManagementService;
    }



    public OperationResult<List<UserAccount>> searchUsers(String keyword, Role role, AccountStatus status) {
        return userManagementService.searchUsers(keyword, role, status);
    }



    public OperationResult<UserAccount> changeRole(String userId, Role newRole) {
        return userManagementService.changeRole(userId, newRole);
    }



    public OperationResult<UserAccount> changeStatus(String userId, AccountStatus newStatus) {
        return userManagementService.changeStatus(userId, newStatus);
    }
}
