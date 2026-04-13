package com.ums.schedule.application.target.upload;

import com.ums.schedule.code.EnumMapperSelector;
import com.ums.schedule.application.target.dto.SendTargetDto;
import com.ums.schedule.domain.target.upload.TargetUpload;
import com.ums.schedule.domain.request.SendRequest;


import java.util.List;

public interface TargetUploadService extends EnumMapperSelector {
    TargetUpload create(SendRequest request, List<SendTargetDto> dtos);
}
