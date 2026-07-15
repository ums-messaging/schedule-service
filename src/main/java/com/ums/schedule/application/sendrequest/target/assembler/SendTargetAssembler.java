package com.ums.schedule.application.sendrequest.target.assembler;

import com.ums.schedule.application.sendrequest.target.data.TargetMessageData;
import com.ums.schedule.domain.request.target.SendTarget;
import com.ums.schedule.domain.request.target.upload.TargetUploadReport;

import java.util.List;

public interface SendTargetAssembler {
    List<SendTarget> assemble(String messageId, TargetUploadReport targetUpload, List<TargetMessageData> targetList);
}
