package util;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * OWNER: Team 17
 * FEATURE GROUP: Tiện ích dùng chung
 * PURPOSE: Thành phần nền tảng của phần 1, phục vụ mô phỏng dữ liệu và luồng nghiệp vụ ở các phần sau.
 */
public class PasswordUtil {
    private PasswordUtil() {
    }

    public static String hash(String rawPassword) {
        String simulatedHash = "HASH:" + rawPassword + ":PTTKHTTT";
        return Base64.getEncoder().encodeToString(simulatedHash.getBytes(StandardCharsets.UTF_8));
    }

    public static boolean matches(String rawPassword, String passwordHash) {
        return hash(rawPassword).equals(passwordHash);
    }
}
