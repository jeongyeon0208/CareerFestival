package careerfestival.career.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    LOGIN_ID_DUPLICATED(HttpStatus.CONFLICT, ""),
    EMAIL_DUPLICATED(HttpStatus.CONFLICT, ""),
    ID_NOT_FOUND(HttpStatus.NOT_FOUND, ""),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "");
    private HttpStatus httpStatus;
    private String message;
}
