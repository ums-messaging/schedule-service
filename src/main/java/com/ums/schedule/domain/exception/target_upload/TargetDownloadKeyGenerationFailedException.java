package com.ums.schedule.domain.exception.target_upload;

public class TargetDownloadKeyGenerationFailedException extends TargetUploadPolicyViolationException {

    protected TargetDownloadKeyGenerationFailedException(String message) {
        super(message);
    }

    public static TargetDownloadKeyGenerationFailedException of() {
        return new TargetDownloadKeyGenerationFailedException("다운로드 키 업로드 경로를 불러오는데 실패했습니다.");
    }
}
