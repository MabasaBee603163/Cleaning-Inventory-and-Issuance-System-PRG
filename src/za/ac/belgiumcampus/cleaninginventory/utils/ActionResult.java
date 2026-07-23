package za.ac.belgiumcampus.cleaninginventory.utils;

/**
 * Simple result wrapper for controllers to return success/error messages to the UI.
 */
public class ActionResult<T> {

    private final boolean success;
    private final String message;
    private final T data;

    private ActionResult(boolean success, String message, T data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

    public static <T> ActionResult<T> ok(String message, T data) {
        return new ActionResult<>(true, message, data);
    }

    public static <T> ActionResult<T> ok(String message) {
        return new ActionResult<>(true, message, null);
    }

    public static <T> ActionResult<T> fail(String message) {
        return new ActionResult<>(false, message, null);
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
