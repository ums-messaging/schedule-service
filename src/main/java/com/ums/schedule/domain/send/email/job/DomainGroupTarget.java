package com.ums.schedule.domain.send.email.job;

import com.ums.schedule.domain.target.SendTarget;

public record DomainGroupTarget(
        DomainGroup group,
        String receiverId,
        String receiverName,
        String receiverEmail,
        String title,
        String content,
        Integer retryCount
) {
    public static DomainGroupTarget of(DomainGroup domainGroup, SendTarget target) {
        return new DomainGroupTarget(
                domainGroup,
                target.getId().toString(),
                target.getTargetName(),
                target.getContact(),
                target.getTitle(),
                target.getContent(),
                target.getAttemptNo()
        );
    }

    public String getDomain() {
        return receiverEmail.split("@")[1];
    }


}
