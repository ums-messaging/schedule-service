package com.ums.schedule.domain.send.email.code;

import com.ums.schedule.common.code.mapper.EnumMapperType;

public enum ContentTypeEnum implements EnumMapperType {
    HTML("text/html", "html"),
    CSV("text/csv", "csv"),
    JSON("application/json", "json"),
    PDF("application/pdf", "pdf"),
    EXCEL_XLX("application/vnd.ms-excel", "xlx"),
    EXCEL_XLSX("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", "xlsx"),
    MULTIPART_MIXED("multipart/mixed", "attachment"),
    MULTIPART_ALTERNATIVE("multipart/alternative", "text/html"),
    IMAGE_PNG("image/png", "png"),
    MULTIPART_RELATED("multipart/related", "html과 html이 참조하는 리소스"),
    PLAIN("text/plain", "text"),
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
