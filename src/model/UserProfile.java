package model;

/**
 * OWNER: Team 17
 * FEATURE GROUP: Model entity dùng chung
 * PURPOSE: Thành phần nền tảng của phần 1, phục vụ mô phỏng dữ liệu và luồng nghiệp vụ ở các phần sau.
 */
public class UserProfile {
    public String id;
    public String userId;
    public String fullName;
    public String bio;

    public UserProfile(String id, String userId, String fullName, String bio) {
        this.id = id;
        this.userId = userId;
        this.fullName = fullName;
        this.bio = bio;
    }

    @Override
    public String toString() {
        return fullName + " | " + bio;
    }
}
