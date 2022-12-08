package guckflix.backend.exception;

public class MemberDuplicateException extends RuntimeException{

    public MemberDuplicateException() {
    }

    public MemberDuplicateException(String message) {
        super(message);
    }

    public MemberDuplicateException(String message, Throwable cause) {
        super(message, cause);
    }

    public MemberDuplicateException(Throwable cause) {
        super(cause);
    }

    public MemberDuplicateException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
