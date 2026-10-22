package com.ums.schedule.email.domain.event;

import com.ums.schedule.email.domain.target.SendTargetError;
import com.ums.schedule.email.domain.target.SendTargetRequest;
import com.ums.schedule.email.domain.target.group.DomainGroup;

import java.util.List;

public record SendTargetResultList (
        Long sendRequestId,
        DomainGroup domainGroup,
        List<SendTargetRequest> successList,
        List<SendTargetError> errorList
) {
    public static SendTargetResultList of(Long sendRequestId, DomainGroup domainGroup, List<SendTargetRequest> results) {
        return new SendTargetResultList(sendRequestId, domainGroup, results, List.of());
    }
    public static SendTargetResultList of(Long sendRequestId, DomainGroup domainGroup, List<SendTargetRequest> results, List<SendTargetError> errors) {
        return new SendTargetResultList(sendRequestId, domainGroup, results, errors);
    }
}
