package com.ums.schedule.email.application.target.reader.listener.model;

import com.github.f4b6a3.tsid.TsidCreator;

public record SendTargetGroup(
        Long groupId,
        String groupKey
) {
    public static SendTargetGroup of(String groupKey) {
        Long groupId = TsidCreator.getTsid().toLong();
        return new SendTargetGroup(groupId, groupKey);
    }
}
