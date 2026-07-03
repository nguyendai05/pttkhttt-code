package repository;

import model.UserAccount;
import model.enums.AccountStatus;
import model.enums.Role;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


public class UserRepository {
    private List<UserAccount> users = new ArrayList<>();

    public void save(UserAccount user) {
        findById(user.getId()).ifPresent(users::remove);
        users.add(user);
    }

    public Optional<UserAccount> findById(String id) {
        return users.stream().filter(user -> user.getId().equalsIgnoreCase(id)).findFirst();
    }

    public Optional<UserAccount> findByUsernameOrEmail(String identity) {
        return users.stream()
                .filter(user -> user.getUsername().equalsIgnoreCase(identity) || user.getEmail().equalsIgnoreCase(identity))
                .findFirst();
    }

    public boolean existsByUsername(String username) {
        return users.stream().anyMatch(user -> user.getUsername().equalsIgnoreCase(username));
    }

    public boolean existsByEmail(String email) {
        return users.stream().anyMatch(user -> user.getEmail().equalsIgnoreCase(email));
    }

    public List<UserAccount> findAll() {
        return new ArrayList<>(users);
    }

    public List<UserAccount> search(String keyword, Role role, AccountStatus status) {
        String lower = keyword == null ? "" : keyword.toLowerCase();
        return users.stream()
                .filter(user -> lower.isEmpty()
                        || user.getId().toLowerCase().contains(lower)
                        || user.getUsername().toLowerCase().contains(lower)
                        || user.getEmail().toLowerCase().contains(lower))
                .filter(user -> role == null || user.getRole() == role)
                .filter(user -> status == null || user.getStatus() == status)
                .collect(Collectors.toList());
    }

    public long countActiveAdmins() {
        return users.stream().filter(user -> user.getRole() == Role.ADMIN && user.getStatus() == AccountStatus.ACTIVE).count();
    }
}
