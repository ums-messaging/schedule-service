package com.ums.schedule.domain.send.email.job;

import com.ums.schedule.common.code.email.EmailCacheKey;

import java.util.List;

public record DomainGroupEntry(Long jobId, Long groupId, List<DomainGroupTarget> groups) {

    public String getKey() {
        return EmailCacheKey.GROUP_ID.value().concat(String.valueOf(groupId));
    }

    public Long getScore() {
        return jobId;
    }


    public List<DomainGroupTarget> getMember() {
        return groups;
    }
}
