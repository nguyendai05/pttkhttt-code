package util;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * OWNER: Team 17
 * FEATURE GROUP: Tiện ích dùng chung
 * PURPOSE: Thành phần nền tảng của phần 1, phục vụ mô phỏng dữ liệu và luồng nghiệp vụ ở các phần sau.
 */
public class InputValidator {
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[\\w._%+-]+@[\\w.-]+\\.[A-Za-z]{2,}$");
    private static final Set<String> ALLOWED_EXTENSIONS = new HashSet<>(Arrays.asList("pdf", "docx", "pptx", "txt"));

    private InputValidator() {
    }

    public static boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    public static boolean isValidEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email.trim()).matches();
    }

    public static boolean isStrongPassword(String password) {
        return password != null && password.length() >= 8;
    }

    public static boolean hasAllowedDocumentExtension(String fileName) {
        if (isBlank(fileName) || !fileName.contains(".")) {
            return false;
        }
        String extension = fileName.substring(fileName.lastIndexOf('.') + 1).toLowerCase();
        return ALLOWED_EXTENSIONS.contains(extension);
    }
}
