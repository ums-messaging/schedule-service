package com.ums.schedule.application.ums.common.send;

import com.ums.schedule.common.code.mapper.EnumMapperSelector;
import com.ums.schedule.domain.send.email.job.SendJob;

public interface JobManager extends EnumMapperSelector {
    void manage(SendJob job);
}
