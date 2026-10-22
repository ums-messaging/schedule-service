package com.ums.schedule.common.code.target_upload;

import com.ums.schedule.common.code.mapper.EnumMapper;
import com.ums.schedule.common.code.mapper.EnumMapperType;
import com.ums.schedule.orchestrator.common.code.SendRequestUploadStatus;
import com.ums.schedule.orchestrator.common.code.UploadType;

public enum TargetUploadReportCode implements EnumMapper {
    UPLOAD_TYPE(UploadType.class),
    UPLOAD_EVENT(TargetUploadEvent.class),
    UPLOAD_STATUS(SendRequestUploadStatus.class),
    UPLOAD_FORMAT(TargetUploadFormatEnum.class)
    ;

    Class<? extends EnumMapperType> code;

    TargetUploadReportCode(Class<? extends EnumMapperType> code) {
        this.code = code;
    }

    @Override
    public String key() {
        return this.name();
    }

    @Override
    public Class<? extends EnumMapperType> code() {
        return this.code;
    }
}
