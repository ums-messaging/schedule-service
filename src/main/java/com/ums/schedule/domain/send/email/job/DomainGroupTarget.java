package com.ums.schedule.domain.send.email.job;

import com.ums.schedule.domain.target.SendTarget;
import com.ums.schedule.domain.target.TargetMessage;

public record DomainGroupTarget(
        DomainGroup group,
        String receiverId,
        String receiverName,
        String receiverEmail,
        String title,
        String content,
        Integer retryCount
) {
    public static DomainGroupTarget of(DomainGroup domainGroup, TargetMessage targetMessage)
    {
//        SendTarget target = targetMessage.getSendTarget();
//        return new DomainGroupTarget(
//                domainGroup,
//                target.getId().toString(),
//                target.getTargetName(),
//                target.getContact(),
//                null,
//                null,
//                target.getAttemptNo()
//        );
        return null;
    }

    public String getDomain() {
        return receiverEmail.split("@")[1];
    }


}
