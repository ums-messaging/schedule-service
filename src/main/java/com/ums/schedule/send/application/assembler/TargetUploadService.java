package com.ums.schedule.send.application.assembler;

import com.ums.schedule.common.code.EnumMapperSelector;
import com.ums.schedule.send.application.model.dto.SendTargetDto;
import com.ums.schedule.send.domain.request.SendRequest;
import com.ums.schedule.send.domain.request.upload.TargetUpload;


import java.util.List;

public interface TargetUploadService extends EnumMapperSelector {
    TargetUpload create(SendRequest request, List<SendTargetDto> dtos);
}
