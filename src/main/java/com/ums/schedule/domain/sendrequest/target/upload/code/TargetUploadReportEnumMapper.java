package com.ums.schedule.domain.sendrequest.target.upload.code;

import com.ums.schedule.common.code.mapper.EnumMapper;
import com.ums.schedule.common.code.mapper.EnumMapperType;

public enum TargetUploadReportEnumMapper implements EnumMapper {
    UPLOAD_TYPE(TargetUploadTypeEnum.class),
    UPLOAD_EVENT(TargetUploadEventEnum.class),
    UPLOAD_STATUS(TargetUploadStatusEnum.class),
    UPLOAD_FORMAT(TargetUploadFormatEnum.class)
    ;

    Class<? extends EnumMapperType> code;

    TargetUploadReportEnumMapper(Class<? extends EnumMapperType> code) {
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
