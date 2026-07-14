package com.ums.schedule.application.sendrequest.target.data;

import com.ums.schedule.common.code.target.TargetColumnEnum;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public record TargetMessageData(
        Map<TargetColumnEnum, String> targetData,
        Map<String, Object> dataParam
) {

    public Map<String, Object> getTargetParam() {
        Map<String, Object> targetParamMap = new HashMap<>();

        Map<String, String> targetData = this.targetData.entrySet()
                .stream()
                .collect(Collectors.toMap(entry -> entry.getKey().value(), Map.Entry::getValue));

        targetParamMap.putAll(targetData);
        targetParamMap.putAll(dataParam);

        return targetParamMap;
    }
}