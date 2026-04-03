package com.ums.schedule.send.code;

import com.ums.schedule.common.code.EnumMapper;
import com.ums.schedule.common.code.EnumMapperType;

public enum SendRequestEnumMapper implements EnumMapper {
    SEND_REQUEST_STATUS(SendRequestStatusEnum.class),
    TARGET_UPLOAD_STATUS(TargetUploadStatusEnum.class),
    TARGET_UPLOAD_TYPE(TargetUploadTypeEnum.class),
    TARGET_COLUMN(TargetColumnEnum.class),
    RESULT_CODE(ResultCodeEnum.class),
    EVENT_TYPE(SendRequestEventEnum.class)
    ;

    Class<? extends EnumMapperType> code;

    SendRequestEnumMapper(Class<? extends EnumMapperType> code) {
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
