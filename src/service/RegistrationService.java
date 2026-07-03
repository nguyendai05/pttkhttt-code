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


public class RegistrationService {
    private UserRepository userRepository;
    private ProfileRepository profileRepository;

    public RegistrationService(UserRepository userRepository, ProfileRepository profileRepository) {
        this.userRepository = userRepository;
        this.profileRepository = profileRepository;
    }



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
