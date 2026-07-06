package com.ums.schedule.domain.sendrequest.target.converter;

import com.ums.schedule.common.code.mapper.EnumMapperConverter;
import com.ums.schedule.domain.sendrequest.target.upload.code.TargetUploadTypeEnum;
import jakarta.persistence.Converter;

@Converter
public class TargetUploadTypeConverter extends EnumMapperConverter {
    public TargetUploadTypeConverter() {
        super(TargetUploadTypeEnum.class);
    }
}
