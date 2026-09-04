package com.ums.schedule.domain.target.upload.converter;

import com.ums.schedule.common.code.target_upload.TargetUploadStatus;
import com.ums.schedule.common.converter.state.StatusStateConverter;
import jakarta.persistence.Converter;

@Converter
public class TargetUploadReportStateConverter extends StatusStateConverter {
    public TargetUploadReportStateConverter() {
        super(TargetUploadStatus.class);
    }
}
