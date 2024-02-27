package careerfestival.career.participate.Exception;

public class UserOrEventNotFoundException extends RuntimeException {
    public UserOrEventNotFoundException(String message) {
        super(message);
    }
}