package util;

import java.nio.charset.StandardCharsets;
import java.util.Base64;


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
