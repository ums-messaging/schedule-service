package com.ums.schedule.application.sendrequest.target.query;

import com.ums.schedule.domain.target.TargetMessage;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public record TargetDuplicatedQuery(
        UUID uploadId,
        Collection<String> targetKeyList,
        Collection<String> contacts

) {
    public static TargetDuplicatedQuery of(UUID uploadId,
                                           List<TargetMessage> targetMessage) {
        List<String> targetKeyList = targetMessage.stream().map(TargetMessage::getTargetKey)
                .toList();
        List<String> contacts = targetMessage.stream().map(TargetMessage::getContact).toList();
        return new TargetDuplicatedQuery(
                uploadId,
                targetKeyList,
                contacts
        );
    }
}
