package repository;

import model.UserProfile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


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
