package com.ums.schedule.common.code.request;

import com.ums.schedule.common.code.common.ChannelTypeEnum;
import com.ums.schedule.common.code.mapper.EnumMapper;
import com.ums.schedule.common.code.mapper.EnumMapperType;
import com.ums.schedule.domain.send.code.ResultCodeEnum;
import com.ums.schedule.common.code.target.TargetColumnEnum;
import com.ums.schedule.common.code.target_upload.TargetUploadFormatEnum;
import com.ums.schedule.common.code.target_upload.TargetUploadStatusEnum;
import com.ums.schedule.common.code.target_upload.TargetUploadTypeEnum;

public enum SendRequestEnumMapper implements EnumMapper {
    CHANNEL_TYPE(ChannelTypeEnum.class),
    SEND_REQUEST_STATUS(SendRequestStatusEnum.class),
    TARGET_UPLOAD_STATUS(TargetUploadStatusEnum.class),
    TARGET_UPLOAD_TYPE(TargetUploadTypeEnum.class),
    TARGET_UPLOAD_FORMAT(TargetUploadFormatEnum.class),
    TARGET_COLUMN(TargetColumnEnum.class),
    RESULT_CODE(ResultCodeEnum.class),
    EVENT_TYPE(SendGroupEventTypeEnum.class)
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
