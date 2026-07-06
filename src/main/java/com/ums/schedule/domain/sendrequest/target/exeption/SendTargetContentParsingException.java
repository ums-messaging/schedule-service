package com.ums.schedule.domain.sendrequest.target.exeption;

public class SendTargetContentParsingException extends SendTargetPolicyViolationException {

    protected SendTargetContentParsingException(Throwable e) {
        super(e);
    }

    public static SendTargetContentParsingException of(Throwable cause) {
        return new SendTargetContentParsingException(cause);
    }

}
