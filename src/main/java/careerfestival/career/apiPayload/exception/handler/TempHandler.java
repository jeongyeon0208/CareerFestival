package careerfestival.career.apiPayload.exception.handler;

import careerfestival.career.apiPayload.code.BaseErrorCode;
import careerfestival.career.apiPayload.exception.GeneralException;

public class TempHandler extends GeneralException {
    public TempHandler(BaseErrorCode errorCode){
        super(errorCode);
    }
}
