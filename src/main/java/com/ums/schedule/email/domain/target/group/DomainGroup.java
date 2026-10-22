package com.ums.schedule.email.domain.target.group;

import com.github.f4b6a3.tsid.TsidCreator;

public record DomainGroup (
        Long groupId,
        String groupKey
) {

    public static DomainGroup of(String groupKey) {
        return new DomainGroup(
                TsidCreator.getTsid().toLong(),
                groupKey
        );
    }

    public static DomainGroup of(long groupId, String groupKey) {

        return new DomainGroup(
                groupId,
                groupKey
        );
    }
}
