package com.ums.schedule.email.application.target.query.model;

import com.ums.schedule.email.domain.target.SendTargetRequest;

import java.util.Collection;
import java.util.List;

public record TargetDuplicatedQuery(
        Long sendRequestId,
        Collection<String> targetKeyList,
        Collection<String> contacts

) {
    public static TargetDuplicatedQuery of(Long sendRequestId, List<SendTargetRequest> targetList) {
        List<String> targetKeyList = targetList.stream()
                .map(v -> v.getReceiver().getTargetKey())
                .toList();

        List<String> contacts = targetList.stream()
                .map(v -> v.getReceiver().getEmail())
                .toList();

        return new TargetDuplicatedQuery(
                sendRequestId,
                targetKeyList,
                contacts
        );
    }
}
