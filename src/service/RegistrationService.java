package service;

import model.UserAccount;
import model.UserProfile;
import model.enums.AccountStatus;
import model.enums.Role;
import repository.ProfileRepository;
import repository.UserRepository;
import util.IdGenerator;
import util.InputValidator;
import util.OperationResult;
import util.PasswordUtil;

/**
 * OWNER: Nguyễn Xuân Đại
 * FEATURE GROUP: Đăng ký tài khoản
 * RELATED USE CASES: UC-1
 * PURPOSE: Xử lý nghiệp vụ đăng ký tài khoản và tạo profile cơ bản.
 */
public class RegistrationService {
    private UserRepository userRepository;
    private ProfileRepository profileRepository;

    public RegistrationService(UserRepository userRepository, ProfileRepository profileRepository) {
        this.userRepository = userRepository;
        this.profileRepository = profileRepository;
    }

    /**
     * OWNER: Nguyễn Xuân Đại
     * USE CASE: UC-1 - Đăng ký
     * ACTOR: Guest
     * FLOW: Basic Flow / Exception Flow
     * PURPOSE: Kiểm tra dữ liệu đăng ký, chống trùng username/email, tạo USER ACTIVE và profile cơ bản.
     * SEQUENCE NOTE: ConsoleView -> RegistrationController -> RegistrationService -> UserRepository/ProfileRepository -> SessionManager.
     */
    public OperationResult<UserAccount> register(String username, String email, String password, String confirmPassword) {
        if (InputValidator.isBlank(username) || InputValidator.isBlank(email)
                || InputValidator.isBlank(password) || InputValidator.isBlank(confirmPassword)) {
            return OperationResult.fail("Vui lòng nhập đầy đủ username, email, password và confirmPassword.");
        }
        if (!InputValidator.isValidEmail(email)) {
            return OperationResult.fail("Email không đúng định dạng.");
        }
        if (!InputValidator.isStrongPassword(password)) {
            return OperationResult.fail("Password phải có tối thiểu 8 ký tự.");
        }
        if (!password.equals(confirmPassword)) {
            return OperationResult.fail("Password và confirmPassword không khớp.");
        }
        if (userRepository.existsByUsername(username)) {
            return OperationResult.fail("Username đã tồn tại.");
        }
        if (userRepository.existsByEmail(email)) {
            return OperationResult.fail("Email đã tồn tại.");
        }

        UserAccount user = new UserAccount(
                IdGenerator.nextId("USR"),
                username.trim(),
                email.trim(),
                PasswordUtil.hash(password),
                Role.USER,
                AccountStatus.ACTIVE
        );
        userRepository.save(user);
        profileRepository.save(new UserProfile(
                IdGenerator.nextId("PRO"),
                user.getId(),
                username.trim(),
                "Hồ sơ cơ bản được tạo khi đăng ký"
        ));
        return OperationResult.ok("Đăng ký thành công. Tài khoản mới có role USER và status ACTIVE.", user);
    }
}
