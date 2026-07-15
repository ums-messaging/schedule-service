package com.ums.schedule.domain.send.email.job;

import com.github.f4b6a3.tsid.TsidCreator;
import com.ums.schedule.common.code.email.EmailCacheKey;

import java.util.List;

public record DomainGroup(
        EmailSendJob job,
        Long groupId,
        String domain
) {
    public static DomainGroup create(EmailSendJob job, String domain) {
        DomainGroup domainGroup = new DomainGroup(job, TsidCreator.getTsid().toLong(), domain);
        return domainGroup;
    }


    public String getKey() {
        return EmailCacheKey.GROUP_ID.value().concat(String.valueOf(groupId));
    }

    public boolean hasLimitSize(List<DomainGroupTarget> groupList, int limitSize) {
        return groupList.size() >= limitSize;
    }

    public List<DomainGroupTarget> filterGroupTarget(List<DomainGroupTarget> groupList) {
        return groupList.stream()
                .filter(target -> target.group().equals(this))
                .toList();
    }
}
