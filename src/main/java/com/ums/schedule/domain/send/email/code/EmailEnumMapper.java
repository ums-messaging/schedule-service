package com.ums.schedule.domain.send.email.code;

import com.ums.schedule.common.code.mapper.EnumMapper;
import com.ums.schedule.common.code.mapper.EnumMapperType;

public enum EmailEnumMapper implements EnumMapper {
    DNS_QUERY_RESULT(DnsQueryResultEnum.class);
    ;
    Class<? extends EnumMapperType> code;

    EmailEnumMapper(Class<DnsQueryResultEnum> code) {
        this.code = code;
    }


    @Override
    public String key() {
        return null;
    }

    @Override
    public Class<? extends EnumMapperType> code() {
        return null;
    }
}
