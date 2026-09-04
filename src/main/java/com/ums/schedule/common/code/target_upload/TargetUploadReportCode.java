package com.ums.schedule.common.code.target_upload;

import com.ums.schedule.common.code.mapper.EnumMapper;
import com.ums.schedule.common.code.mapper.EnumMapperType;

public enum TargetUploadReportCode implements EnumMapper {
    UPLOAD_TYPE(TargetUploadType.class),
    UPLOAD_EVENT(TargetUploadEvent.class),
    UPLOAD_STATUS(TargetUploadStatus.class),
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
