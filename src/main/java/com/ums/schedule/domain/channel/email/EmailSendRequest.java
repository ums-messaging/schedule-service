package com.ums.schedule.domain.channel.email;

import com.ums.schedule.code.send.ContentTypeEnum;
import com.ums.schedule.code.send.TargetColumnEnum;
import com.ums.schedule.domain.channel.email.message.EmailBody;
import com.ums.schedule.domain.request.SendRequest;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Map;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EmailSendRequest extends SendRequest {
    private EmailBody body;
    private String mailFrom;
    private String mailFromName;

    public static EmailSendRequest of(EmailBody body) {
        EmailSendRequest request = new EmailSendRequest();
        request.setEmailBody(body);
        return request;
    }

    private void setEmailBody(EmailBody body) {
        this.body = body;
    }

    @Override
    protected String resolvedContactByChannel(Map<TargetColumnEnum, String> targetData) {
        return targetData.get(TargetColumnEnum.TARGET_EMAIL);
    }

    @Override
    protected ContentTypeEnum resolveContentType() {
        return null;
    }
}
