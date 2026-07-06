package com.ums.schedule.application.sendrequest.target.data;

import com.ums.schedule.domain.sendrequest.target.code.TargetColumnEnum;

import java.util.Map;

public record TargetMessageData(
        Map<TargetColumnEnum, String> targetData,
        Map<String, Object> dataParam
) {
}