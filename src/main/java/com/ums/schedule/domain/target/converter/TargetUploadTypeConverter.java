package com.ums.schedule.domain.target.converter;

import com.ums.schedule.code.EnumMapperConverter;
import com.ums.schedule.code.EnumMapperType;
import com.ums.schedule.code.send.TargetUploadTypeEnum;
import jakarta.persistence.Converter;

@Converter
public class TargetUploadTypeConverter extends EnumMapperConverter {
    public TargetUploadTypeConverter() {
        super(TargetUploadTypeEnum.class);
    }
}
