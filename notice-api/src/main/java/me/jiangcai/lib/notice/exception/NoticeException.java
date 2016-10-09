package me.jiangcai.lib.notice.exception;

/**
 * @author CJ
 */
public class NoticeException extends RuntimeException {
    public NoticeException() {
    }

    public NoticeException(String message) {
        super(message);
    }

    public NoticeException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoticeException(Throwable cause) {
        super(cause);
    }

    public NoticeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
