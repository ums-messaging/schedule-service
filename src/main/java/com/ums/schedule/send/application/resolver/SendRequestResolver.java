package com.ums.schedule.send.application.resolver;

import com.ums.schedule.common.code.EnumMapperSelector;
import com.ums.schedule.send.domain.request.SendRequest;
import com.ums.schedule.send.domain.request.upload.TargetUpload;

public interface SendRequestResolver extends EnumMapperSelector {
    TargetUpload resolve(SendRequest sendRequest);
}
