package util;

import java.util.Random;


public class OtpGenerator {
    private static final Random RANDOM = new Random();

    private OtpGenerator() {
    }

    public static String generate() {
        return String.valueOf(100000 + RANDOM.nextInt(900000));
    }
}
