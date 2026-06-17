package com.ums.schedule.application.sendrequest.target.processor;

import com.ums.schedule.adapter.api.request.request.SendRequestCreateRequest;
import com.ums.schedule.application.sendrequest.target.result.TargetUploadResult;
import com.ums.schedule.common.code.mapper.EnumMapperSelector;
import com.ums.schedule.domain.sendrequest.target.upload.TargetUploadReport;

public interface TargetUploadProcessor extends EnumMapperSelector  {
    TargetUploadResult requestUpload(TargetUploadReport targetUploadReport, SendRequestCreateRequest request, String messageId);
}
