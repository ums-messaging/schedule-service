package com.ums.schedule.application.channel;

import com.ums.schedule.code.EnumMapperSelector;
import com.ums.schedule.domain.request.SendRequest;

public interface JobCreator extends EnumMapperSelector {
    SendJob createJob(SendRequest request);
}
