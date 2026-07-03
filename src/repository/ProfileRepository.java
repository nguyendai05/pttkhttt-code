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
    private List<UserProfile> profiles = new ArrayList<>();

    public void save(UserProfile profile) {
        findByUserId(profile.getUserId()).ifPresent(profiles::remove);
        profiles.add(profile);
    }

    public Optional<UserProfile> findByUserId(String userId) {
        return profiles.stream().filter(profile -> profile.getUserId().equals(userId)).findFirst();
    }
}
