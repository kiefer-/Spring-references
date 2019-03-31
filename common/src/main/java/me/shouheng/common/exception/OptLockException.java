package me.shouheng.common.exception;

/**
 * @author shouh, 2019/3/31-15:48
 */
public class OptLockException extends RuntimeException {

    public OptLockException() {
    }

    public OptLockException(String message) {
        super(message);
    }

    public OptLockException(String message, Throwable cause) {
        super(message, cause);
    }

    public OptLockException(Throwable cause) {
        super(cause);
    }

    public OptLockException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
