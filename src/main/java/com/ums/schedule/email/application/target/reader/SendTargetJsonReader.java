package com.ums.schedule.email.application.target.reader;

import com.ums.schedule.common.code.mapper.EnumMapperValue;
import com.ums.schedule.orchestrator.common.code.UploadType;
import com.ums.schedule.email.application.target.reader.model.SendTargetReadContext;
import com.ums.schedule.email.application.target.reader.model.SendTargetsReadResult;

public class SendTargetJsonReader implements SendTargetReader {
    @Override
    public boolean supports(EnumMapperValue type) {
        return UploadType.JSON == UploadType.valueOf(type.code());
    }

    @Override
    public SendTargetsReadResult read(String objectKey, SendTargetReadContext context) {
        return null;
    }
}
