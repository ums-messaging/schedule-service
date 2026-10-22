package com.ums.schedule.email.application.target.reader.requests.result;

import com.ums.schedule.email.common.code.SendTargetRequestStatusCode;
import com.ums.schedule.email.domain.target.SendTargetRequest;
import com.ums.schedule.email.domain.target.group.DomainGroup;

import java.util.List;

public record TargetUploadResultList(
        DomainGroup group,
        List<SendTargetRequest> completedTargetList,
        List<SendTargetRequest> failedTargetList
) {

    public static TargetUploadResultList of(DomainGroup group, List<SendTargetRequest> results) {
        List<SendTargetRequest> completedList = results.stream()
                .filter(v -> !(v.getState().currentStateCode() == SendTargetRequestStatusCode.ERROR))
                .toList();
        List<SendTargetRequest> failedList = results.stream()
                .filter(v -> (v.getState().currentStateCode() == SendTargetRequestStatusCode.ERROR))
                .toList();

        return new TargetUploadResultList(
                group,
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
