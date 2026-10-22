package com.ums.schedule.orchestrator.domain.exception;

import com.ums.schedule.common.code.api.ErrorCode;
import com.ums.schedule.common.exception.BusinessException;

public class SendRequestUploadDomainException extends BusinessException {
    protected SendRequestUploadDomainException(ErrorCode errorCode, Object... args) {
        super(errorCode, args);
    }
}
