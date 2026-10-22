package com.ums.schedule.email.application.target.reader;

import com.ums.schedule.email.application.target.reader.model.SendTargetReadContext;
import com.ums.schedule.email.application.target.reader.model.SendTargetsReadResult;
import com.ums.schedule.common.code.mapper.EnumMapperSelector;

public interface SendTargetReader extends EnumMapperSelector {
    SendTargetsReadResult read(String objectKey, SendTargetReadContext context);
}
