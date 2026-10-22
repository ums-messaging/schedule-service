package com.ums.schedule.email.domain.event;

import com.ums.schedule.common.code.request.SendGroupEventType;
import com.ums.schedule.email.domain.target.group.DomainGroup;


public record DomainGroupEvent (
        Long sendRequestId,
        SendGroupEventType eventType,
        DomainGroup domainGroup
) {
    public static DomainGroupEvent of(Long sendRequestId,
                                      SendGroupEventType eventType,
                                      DomainGroup domainGroup

                                      ) {
        return new DomainGroupEvent(
                sendRequestId,
                eventType,
                domainGroup
        );
    }
}
