package com.ums.schedule.application.sendrequest.target.result;

import com.ums.schedule.domain.target.SendTarget;
import com.ums.schedule.common.code.target.SendTargetStatus;

import java.util.List;
import java.util.Map;

public record SendTargetSaveResult(
        List<SendTarget> completedTargetList,
        List<SendTarget> failedTargetList
) {
    public static SendTargetSaveResult of(List<SendTarget> targetList) {
        List<SendTarget> completedList = targetList.stream()
                .filter(target -> target.getState().getCurrentCode() != SendTargetStatus.FAIL)
                .toList();
        List<SendTarget> failedList = targetList.stream()
                .filter(target -> target.getState().getCurrentCode() == SendTargetStatus.FAIL)
                .toList();
        return new SendTargetSaveResult(completedList, failedList);
    }

    public static SendTargetSaveResult of(Map<SendTargetStatus, List<SendTarget>> targetListMap) {
        List<SendTarget> failedList = targetListMap.entrySet()
                .stream()
                .filter(entry -> entry.getKey() == SendTargetStatus.FAIL)
                .map(entry -> entry.getValue())
                .findAny()
                .orElse(List.of());
        List<SendTarget> completedList = targetListMap.entrySet()
                .stream()
                .filter(entry -> entry.getKey() != SendTargetStatus.FAIL)
                .map(entry -> entry.getValue())
                .findAny()
                .orElse(List.of());
        return new SendTargetSaveResult(completedList, failedList);
    }
}
