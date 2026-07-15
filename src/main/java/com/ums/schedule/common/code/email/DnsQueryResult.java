package com.ums.schedule.common.code.email;

import com.ums.schedule.common.code.mapper.EnumMapperType;
import org.xbill.DNS.Lookup;

public enum DnsQueryResult implements EnumMapperType {
    SUCCESS(Lookup.SUCCESSFUL, "Success. "),
    UNRECOVERABLE(Lookup.UNRECOVERABLE, "Unecoverable."),
    TRY_AGAIN(Lookup.TRY_AGAIN, "Try Again."),
    HOST_NOT_FOUND(Lookup.HOST_NOT_FOUND, "Host Not Found."),
    TYPE_NOT_FOUND(Lookup.TYPE_NOT_FOUND, "Type Not Found")
    ,
    ;

    Integer value;
    String description;

    DnsQueryResult(Integer value, String description) {
        this.value = value;
        this.description = description;
    }


    @Override
    public String code() {
        return this.name();
    }

    @Override
    public String value() {
        return String.valueOf(this.value);
    }

    @Override
    public String description() {
        return null;
    }
}
