package com.ums.schedule.domain.target.exeption.upload;

public abstract class TargetUploadRequiredException extends TargetUploadException {

    protected TargetUploadRequiredException(String message) {
        super(String.format("%s는 필수 값입니다.", message));
    }


}
