package com.ums.schedule.application.send;

import com.ums.schedule.common.code.mapper.EnumMapperSelector;

public interface JobWorker extends EnumMapperSelector {
    void work(String groupId);
}
