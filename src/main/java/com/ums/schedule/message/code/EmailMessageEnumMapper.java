package com.ums.schedule.message.code;

import com.ums.schedule.common.code.EnumMapper;
import com.ums.schedule.common.code.EnumMapperType;

public enum EmailMessageEnumMapper implements EnumMapper {
    CHANNEL_TYPE(ChannelTypeEnum.class),
    CHARSET(CharsetEnum.class),
    CONTENT_TYPE(ContentTypeEnum.class),
    ENCODING_TYPE(EncodingTypeEnum.class)

    ;

    Class<? extends EnumMapperType> code;

    EmailMessageEnumMapper(Class<? extends EnumMapperType> code) {
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
