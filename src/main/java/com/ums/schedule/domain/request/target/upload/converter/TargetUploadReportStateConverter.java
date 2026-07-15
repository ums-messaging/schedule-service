package com.ums.schedule.domain.request.target.upload.converter;

import com.ums.schedule.common.code.target_upload.TargetUploadStatusEnum;
import com.ums.schedule.common.converter.StatusStateConverter;
import jakarta.persistence.Converter;

@Converter
public class TargetUploadReportStateConverter extends StatusStateConverter {
    public TargetUploadReportStateConverter() {
        super(TargetUploadStatusEnum.class);
    }
}
