package com.ums.schedule.domain.sendrequest.target.upload.exception;

public class TargetUploadKeyGenerationFailedException extends TargetUploadPolicyViolationException {
    protected TargetUploadKeyGenerationFailedException(String reason) {
        super("upload key could not generate. [%s] ".formatted(reason));
    }

    public static TargetUploadKeyGenerationFailedException of(String reason) {
        return new TargetUploadKeyGenerationFailedException(reason);
    }
    public static TargetUploadKeyGenerationFailedException of() {
        return new TargetUploadKeyGenerationFailedException("업로드 키 경로를 가져오는데 실패했습니다.");
    }
}
