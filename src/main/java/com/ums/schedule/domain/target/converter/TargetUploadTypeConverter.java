package com.ums.schedule.domain.target.converter;

import com.ums.schedule.common.code.mapper.EnumMapperConverter;
import com.ums.schedule.common.code.target_upload.TargetUploadType;
import jakarta.persistence.Converter;

@Converter
public class TargetUploadTypeConverter extends EnumMapperConverter {
    public TargetUploadTypeConverter() {
        super(TargetUploadType.class);
    }
}
