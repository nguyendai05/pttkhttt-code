package util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class DateTimeUtil {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private DateTimeUtil() {
    }

    public static String format(LocalDateTime dateTime) {
        return dateTime == null ? "" : FORMATTER.format(dateTime);
    }
}
