package util;

/**
 * OWNER: Team 17
 * FEATURE GROUP: Tiện ích dùng chung
 * PURPOSE: Chuẩn hóa kết quả trả về từ service/controller cho phần 2.
 */
public class OperationResult<T> {
    private final boolean success;
    private final String message;
    private final T data;

    private OperationResult(boolean success, String message, T data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

    public static <T> OperationResult<T> ok(String message, T data) {
        return new OperationResult<>(true, message, data);
    }

    public static <T> OperationResult<T> fail(String message) {
        return new OperationResult<>(false, message, null);
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }
}
