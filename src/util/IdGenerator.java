package util;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * OWNER: Team 17
 * FEATURE GROUP: Tiện ích dùng chung
 * PURPOSE: Thành phần nền tảng của phần 1, phục vụ mô phỏng dữ liệu và luồng nghiệp vụ ở các phần sau.
 */
public class IdGenerator {
    private static final AtomicInteger COUNTER = new AtomicInteger(1000);

    private IdGenerator() {
    }

    public static String nextId(String prefix) {
        return prefix + "-" + COUNTER.incrementAndGet();
    }
}
