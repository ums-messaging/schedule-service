package com.ums.schedule.email.application.send.mime;

import com.ums.schedule.email.application.send.mime.model.MimeMessage;
import com.ums.schedule.email.domain.event.SendTargetResultList;
import com.ums.schedule.email.domain.target.SendTargetRequest;
import com.ums.schedule.email.domain.target.group.DomainGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public record MessageContext (
        MimeMessage message,
        Map<Integer, List<SendTargetRequest>> targetList
) {
    public static MessageContext of(MimeMessage message, Map<Integer, List<SendTargetRequest>> targetGroupList) {
        return new MessageContext(message, targetGroupList);
    }

    public SendTargetResultList targetResultList(Long sendRequestId, DomainGroup domainGroup) {
        List<SendTargetRequest> list = new ArrayList<>();
        targetList.entrySet().stream()
                .forEach(entry -> {
                    list.addAll(entry.getValue());
                });
        return SendTargetResultList.of(sendRequestId, domainGroup, list);
    }
}
