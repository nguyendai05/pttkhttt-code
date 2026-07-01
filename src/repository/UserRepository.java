package repository;

import model.UserAccount;
import model.enums.AccountStatus;
import model.enums.Role;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * OWNER: Team 17
 * FEATURE GROUP: Repository in-memory dùng chung
 * PURPOSE: Thành phần nền tảng của phần 1, phục vụ mô phỏng dữ liệu và luồng nghiệp vụ ở các phần sau.
 */
public class UserRepository {
    private final List<UserAccount> users = new ArrayList<>();

    public void save(UserAccount user) {
        findById(user.id).ifPresent(users::remove);
        users.add(user);
    }

    public Optional<UserAccount> findById(String id) {
        return users.stream().filter(user -> user.id.equalsIgnoreCase(id)).findFirst();
    }

    public Optional<UserAccount> findByUsernameOrEmail(String identity) {
        return users.stream()
                .filter(user -> user.username.equalsIgnoreCase(identity) || user.email.equalsIgnoreCase(identity))
                .findFirst();
    }

    public boolean existsByUsername(String username) {
        return users.stream().anyMatch(user -> user.username.equalsIgnoreCase(username));
    }

    public boolean existsByEmail(String email) {
        return users.stream().anyMatch(user -> user.email.equalsIgnoreCase(email));
    }

    public List<UserAccount> findAll() {
        return new ArrayList<>(users);
    }

    public List<UserAccount> search(String keyword, Role role, AccountStatus status) {
        String lower = keyword == null ? "" : keyword.toLowerCase();
        return users.stream()
                .filter(user -> lower.isEmpty()
                        || user.id.toLowerCase().contains(lower)
                        || user.username.toLowerCase().contains(lower)
                        || user.email.toLowerCase().contains(lower))
                .filter(user -> role == null || user.role == role)
                .filter(user -> status == null || user.status == status)
                .collect(Collectors.toList());
    }

    public long countActiveAdmins() {
        return users.stream().filter(user -> user.role == Role.ADMIN && user.status == AccountStatus.ACTIVE).count();
    }
}
