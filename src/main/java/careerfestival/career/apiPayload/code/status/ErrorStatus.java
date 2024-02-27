package careerfestival.career.apiPayload.code.status;

import careerfestival.career.apiPayload.code.BaseErrorCode;
import careerfestival.career.apiPayload.code.ErrorReasonDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
@Getter
@AllArgsConstructor
public enum ErrorStatus implements BaseErrorCode {
    // common response
    _INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON500","서버 에러, 관리자에게 문의 바람니다."),

    _BAD_REQUEST(HttpStatus.BAD_REQUEST, "COMMON400","잘못된 요청입니다."),

    _UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "COMMON401","인증이 필요합니다."),

    _FORBIDDEN(HttpStatus.FORBIDDEN, "COMMON403","금지된 요청입니다."),
    // event relative
    NOT_ONGOING_EVENT(HttpStatus.BAD_REQUEST, "EVENT4001", "진행 중인 행사가 아닙니다."),

    MEMBER_EVENT_NOT_FOUND(HttpStatus.NOT_FOUND, "MEMBER_EVENT4002", "해당 행사가 존재하지 않습니다."),

    // ex
    ARTICLE_NOT_FOUND(HttpStatus.NOT_FOUND, "ARTICLE4001", "게시글이 없습니다."),
    // For test
    TEMP_EXCEPTION(HttpStatus.BAD_REQUEST, "TEMP4001", "이거는 테스트");


    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public ErrorReasonDTO getReason() {
        return ErrorReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .build();
    }

    @Override
    public ErrorReasonDTO getReasonHttpStatus() {
        return ErrorReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .httpStatus(httpStatus)
                .build()
                ;
    }
}
