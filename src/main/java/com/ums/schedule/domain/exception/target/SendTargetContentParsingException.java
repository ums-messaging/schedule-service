package com.ums.schedule.domain.exception.target;

public class SendTargetContentParsingException extends SendTargetPolicyViolationException {

    protected SendTargetContentParsingException(Throwable e) {
        super(e);
    }

    public static SendTargetContentParsingException of(Throwable cause) {
        return new SendTargetContentParsingException(cause);
    }

}
