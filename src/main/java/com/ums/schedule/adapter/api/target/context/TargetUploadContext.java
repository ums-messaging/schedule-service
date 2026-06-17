package com.ums.schedule.adapter.api.target.context;

import com.ums.schedule.application.sendrequest.target.data.TargetMessageData;
import com.ums.schedule.domain.sendrequest.target.upload.TargetUploadReport;

import java.util.List;

public record TargetUploadContext(
        TargetUploadReport targetUpload,
        List<TargetMessageData> targetDataList
) {
    public static TargetUploadContext of(TargetUploadReport targetUpload, List<TargetMessageData> targetList) {
        return new TargetUploadContext(targetUpload, targetList);
    }
}
