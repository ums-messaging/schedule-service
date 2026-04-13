package com.ums.schedule.application.target.dto;

import com.ums.schedule.code.send.TargetColumnEnum;

import java.util.Map;

public interface TargetDataTransfer {
    Map<String, Object> extractMessageVariable();
    Map<TargetColumnEnum, String> resolveTargetData();
}
