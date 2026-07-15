package com.ums.schedule.domain.request.target.converter;

import com.ums.schedule.common.code.mapper.EnumMapperConverter;
import com.ums.schedule.common.code.target_upload.TargetUploadTypeEnum;
import jakarta.persistence.Converter;

@Converter
public class TargetUploadTypeConverter extends EnumMapperConverter {
    public TargetUploadTypeConverter() {
        super(TargetUploadTypeEnum.class);
    }
}
