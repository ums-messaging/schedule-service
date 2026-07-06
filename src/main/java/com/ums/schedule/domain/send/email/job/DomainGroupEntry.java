package com.ums.schedule.domain.send.email.job;

import com.ums.schedule.domain.send.email.code.EmailCacheKeyCode;

import java.util.List;

public record DomainGroupEntry(Long jobId, Long groupId, List<DomainGroupTarget> groups) {

    public String getKey() {
        return EmailCacheKeyCode.GROUP_ID.value().concat(String.valueOf(groupId));
    }

    public Long getScore() {
        return jobId;
    }


    public List<DomainGroupTarget> getMember() {
        return groups;
    }
}
