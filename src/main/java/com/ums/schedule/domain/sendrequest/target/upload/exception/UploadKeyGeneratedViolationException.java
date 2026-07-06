package com.ums.schedule.domain.sendrequest.target.upload.exception;

public class UploadKeyGeneratedViolationException extends TargetUploadPolicyException {
    protected UploadKeyGeneratedViolationException(String reason) {
        super("upload key could not generate. [%s] ".formatted(reason));
    }

    public static UploadKeyGeneratedViolationException of(String reason) {
        return new UploadKeyGeneratedViolationException(reason);
    }
}
