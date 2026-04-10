package com.ums.schedule.send.application.model.dto;

import com.ums.schedule.send.code.TargetColumnEnum;

import java.util.Map;

public interface TargetDataTransfer {
    Map<String, Object> extractMessageVariable();
    Map<TargetColumnEnum, String> resolveTargetData();
}
