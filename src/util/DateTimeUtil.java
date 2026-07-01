package util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * OWNER: Team 17
 * FEATURE GROUP: Tiện ích dùng chung
 * PURPOSE: Thành phần nền tảng của phần 1, phục vụ mô phỏng dữ liệu và luồng nghiệp vụ ở các phần sau.
 */
public class DateTimeUtil {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private DateTimeUtil() {
    }

    public static String format(LocalDateTime dateTime) {
        return dateTime == null ? "" : FORMATTER.format(dateTime);
    }
}
