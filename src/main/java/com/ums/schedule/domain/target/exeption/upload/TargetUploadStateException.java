package com.ums.schedule.domain.target.exeption.upload;


import com.ums.schedule.code.send.TargetUploadStatusEnum;
import com.ums.schedule.domain.target.exeption.upload.TargetUploadException;

public abstract class TargetUploadStateException extends TargetUploadException {

    protected TargetUploadStateException(TargetUploadStatusEnum from, TargetUploadStatusEnum to) {
        super(String.format("%s -> %s 로의 상태변경은 불가능합니다. ", from.description(), to.description()));
    }
    protected TargetUploadStateException(String message){
        super(message);
    }

}
