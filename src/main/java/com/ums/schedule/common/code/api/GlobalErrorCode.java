package com.ums.schedule.common.code.api;

import com.ums.schedule.common.code.mapper.EnumMapper;
import com.ums.schedule.common.code.mapper.EnumMapperType;

public enum GlobalErrorCode implements EnumMapper {
    SEND_REQUEST(SendRequestErrorCode.class),
    EMAIL_SEND_REQUEST(EmailSendRequestErrorCode.class),
    SCHEDULE(ScheduleErrorCode.class),
    SEND_MESSAGE(SendMessageErrorCode.class),
    TEMPLATE(TemplateErrorCode.class),
    STATE(StateErrorCode.class)
    ;

    Class<? extends EnumMapperType> clazz;

    GlobalErrorCode(Class<? extends EnumMapperType> clazz) {
        this.clazz = clazz;
    }

    @Override
    public String key() {
        return this.name();
    }

    @Override
    public Class<? extends EnumMapperType> code() {
        return clazz;
    }
}
