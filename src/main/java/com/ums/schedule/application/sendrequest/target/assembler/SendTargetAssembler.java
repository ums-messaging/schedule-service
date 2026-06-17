package com.ums.schedule.application.sendrequest.target.assembler;

import com.ums.schedule.application.sendrequest.target.data.TargetMessageData;
import com.ums.schedule.domain.sendrequest.target.code.SendTargetStatusEnum;
import com.ums.schedule.domain.sendrequest.template.ChannelTemplate;
import com.ums.schedule.domain.sendrequest.target.SendTarget;
import com.ums.schedule.domain.sendrequest.target.upload.TargetUploadReport;

import java.util.List;
import java.util.Map;

public interface SendTargetAssembler<T extends ChannelTemplate> {
    Map<SendTargetStatusEnum, List<SendTarget>> assemble(String messageId, TargetUploadReport targetUpload, List<TargetMessageData> targetList) ;
}
