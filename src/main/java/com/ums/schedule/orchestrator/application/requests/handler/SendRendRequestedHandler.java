package com.ums.schedule.orchestrator.application.requests.handler;

import com.ums.schedule.common.code.mapper.EnumMapperSelector;
import com.ums.schedule.orchestrator.application.requests.result.SendRequestRequestContext;

public interface SendRendRequestedHandler extends EnumMapperSelector {
    void handle(SendRequestRequestContext context);
}