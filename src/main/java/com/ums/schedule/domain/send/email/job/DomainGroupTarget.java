package com.ums.schedule.domain.send.email.job;

import com.ums.schedule.adapter.persistence.TargetGroupQueryResult;
import com.ums.schedule.common.util.JsonUtil;
import com.ums.schedule.domain.target.SendTarget;
import com.ums.schedule.domain.target.TargetMessage;
import com.ums.schedule.domain.target.message.EmailTargetMessage;

import java.util.List;

public record DomainGroupTarget(
        DomainGroup group,
        Long receiverId,
        String receiverName,
        String receiverEmail,
        String title,
        String headerMessage,
        String bodyMessage,
        String footerMessage,
        String attachments
) {
    public static DomainGroupTarget of(DomainGroup domainGroup, TargetGroupQueryResult targetMessage)
    {
        return new DomainGroupTarget(
                domainGroup,
                targetMessage.id(),
                targetMessage.targetName(),
                targetMessage.contact(),
                targetMessage.subject(),
                targetMessage.headerMessage(),
                targetMessage.bodyMessage(),
                targetMessage.footerMessage(),
                JsonUtil.toJson(targetMessage.attachments())
        );
    }

    public String getDomain() {
        return receiverEmail.split("@")[1];
    }


}
