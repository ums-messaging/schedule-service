package com.ums.schedule.orchestrator.domain.upload.converter;

import com.ums.schedule.orchestrator.common.code.SendRequestUploadStatus;
import com.ums.schedule.common.converter.state.StatusStateConverter;
import jakarta.persistence.Converter;

@Converter
public class TargetUploadReportStateConverter extends StatusStateConverter {
    public TargetUploadReportStateConverter() {
        super(SendRequestUploadStatus.class);
    }
}
