package util;

import java.util.Random;

/**
 * OWNER: Team 17
 * FEATURE GROUP: Tiện ích dùng chung
 * PURPOSE: Thành phần nền tảng của phần 1, phục vụ mô phỏng dữ liệu và luồng nghiệp vụ ở các phần sau.
 */
public class OtpGenerator {
    private static final Random RANDOM = new Random();

    private OtpGenerator() {
    }

    public static String generate() {
        return String.valueOf(100000 + RANDOM.nextInt(900000));
    }
}
