package util;


public class OperationResult<T> {
    private boolean success;
    private String message;
    private T data;

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
