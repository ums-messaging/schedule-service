package com.ums.schedule.application.target.upload;


import com.ums.schedule.code.EnumMapperSelector;
import com.ums.schedule.domain.target.upload.TargetUpload;

public interface TargetUploadCreator extends EnumMapperSelector  {
    TargetUpload create(TargetUploadCreateCommand command);
}
