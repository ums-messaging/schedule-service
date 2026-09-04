package com.ums.schedule.application.sendrequest.target.result;

import com.ums.schedule.common.code.target.SendTargetRowStatus;
import com.ums.schedule.domain.target.SendTarget;
import com.ums.schedule.common.code.target.SendTargetStatus;
import com.ums.schedule.domain.target.TargetMessage;

import java.util.List;

public record TargetUploadResultList(
        List<TargetMessage> completedTargetList,
        List<TargetMessage> failedTargetList
) {
    public static TargetUploadResultList batchOf(List<TargetMessage> targetMessages) {
        List<TargetMessage> snapshot = List.copyOf(targetMessages);
        List<TargetMessage> completedList = snapshot.stream()
                .filter(v -> v.getState().getCurrentCode() == SendTargetStatus.COMPLETED)
                .toList();
        List<TargetMessage> failedList = snapshot.stream()
                .filter(v -> v.getState().getCurrentCode() == SendTargetStatus.FAIL)
                .toList();
        return new TargetUploadResultList(completedList, failedList);
    }

    public static TargetUploadResultList of(List<TargetMessage> result) {
        List<TargetMessage> results = List.copyOf(result);
        List<TargetMessage> completedList = results.stream()
                .filter(r -> r.getState().getCurrentCode() == SendTargetStatus.CREATE)
                .toList();
        List<TargetMessage> failedList = results.stream()
                .filter(r -> r.getState().getCurrentCode() == SendTargetStatus.FAIL)
                .toList();

        return new TargetUploadResultList(
                completedList,
                failedList
        );
    }

    public Integer succeedCount() {
        return this.completedTargetList.size();
    }

    public Integer failedCount() {
        return this.failedTargetList.size();
    }
}
