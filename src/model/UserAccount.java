package model;

import model.enums.AccountStatus;
import model.enums.Role;
import util.DateTimeUtil;

import java.time.LocalDateTime;

/**
 * OWNER: Team 17
 * FEATURE GROUP: Model entity dùng chung
 * PURPOSE: Thành phần nền tảng của phần 1, phục vụ mô phỏng dữ liệu và luồng nghiệp vụ ở các phần sau.
 */
public class UserAccount {
    public String id;
    public String username;
    public String email;
    public String passwordHash;
    public Role role;
    public AccountStatus status;
    public LocalDateTime createdAt;
    public LocalDateTime lastLoginAt;

    public UserAccount(String id, String username, String email, String passwordHash, Role role, AccountStatus status) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.passwordHash = passwordHash;
        this.role = role;
        this.status = status;
        this.createdAt = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return id + " | " + username + " | " + email + " | " + role + " | " + status
                + " | lastLogin=" + DateTimeUtil.format(lastLoginAt);
    }
}
