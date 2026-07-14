package com.ums.schedule.common.code.email;


import com.ums.schedule.common.code.mapper.EnumMapperType;

public enum PermissionMaskEnum implements EnumMapperType  {
    ALL("ALL", "모두 허용"),
    COPY("COPY", "복사 허용"),
    PRINT("PRINT", "프린트 허용"),
    WRITE("WRTIE", "수정 허용"),
    NONE("NONE", "허용불가")
    ;

    String value;
    String description;

    PermissionMaskEnum(String value, String description) {
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
