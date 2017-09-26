package me.jiangcai.lib.notice.exception;

import me.jiangcai.lib.notice.To;

/**
 * to无效的异常
 *
 * @author CJ
 * @since 3.1
 */
public class BadToException extends NoticeException {
    private final To to;

    public BadToException(To to) {
        this.to = to;
    }

    public BadToException(String message, To to) {
        super(message);
        this.to = to;
    }

    public BadToException(String message, Throwable cause, To to) {
        super(message, cause);
        this.to = to;
    }

    public BadToException(Throwable cause, To to) {
        super(cause);
        this.to = to;
    }

    public BadToException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, To to) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.to = to;
    }
}
