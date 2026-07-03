package util;

import java.util.concurrent.atomic.AtomicInteger;


public class IdGenerator {
    private static final AtomicInteger COUNTER = new AtomicInteger(1000);

    private IdGenerator() {
    }

    public static String nextId(String prefix) {
        return prefix + "-" + COUNTER.incrementAndGet();
    }
}
