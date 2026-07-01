package repository;

import model.UserProfile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * OWNER: Team 17
 * FEATURE GROUP: Repository in-memory dùng chung
 * PURPOSE: Thành phần nền tảng của phần 1, phục vụ mô phỏng dữ liệu và luồng nghiệp vụ ở các phần sau.
 */
public class ProfileRepository {
    private final List<UserProfile> profiles = new ArrayList<>();

    public void save(UserProfile profile) {
        findByUserId(profile.userId).ifPresent(profiles::remove);
        profiles.add(profile);
    }

    public Optional<UserProfile> findByUserId(String userId) {
        return profiles.stream().filter(profile -> profile.userId.equals(userId)).findFirst();
    }
}
