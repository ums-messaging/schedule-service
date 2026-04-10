package com.ums.schedule.message.code;

import com.ums.schedule.common.code.EnumMapperType;

public enum ContentTypeEnum implements EnumMapperType  {
    HTML("text/html", ""),
    CSV("text/csv", ""),
    JSON("application/json", ""),
    PDF("application/pdf", ""),
    EXCEL_XLX("application/vnd.ms-excel", ""),
    EXCEL_XLSX("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", ""),
    PLAIN("plain/text", "")
    ;


    String value;
    String description;

    ContentTypeEnum(String value, String description) {
        this.value = value;
        this.description = description;
    }


    @Override
    public String code() {
        return this.name();
    }

    @Override
    public String value() {
        return this.value;
    }

    @Override
    public String description() {
        return this.description;
    }
}
