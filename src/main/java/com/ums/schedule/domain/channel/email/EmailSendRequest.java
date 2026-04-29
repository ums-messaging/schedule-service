package com.ums.schedule.domain.channel.email;

import com.ums.schedule.code.send.ContentTypeEnum;
import com.ums.schedule.code.send.TargetColumnEnum;
import com.ums.schedule.domain.channel.email.message.EmailBody;
import com.ums.schedule.domain.request.SendRequest;
import com.ums.schedule.domain.target.upload.TargetUpload;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Map;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class EmailSendRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    @MapsId
    @OneToOne
    private SendRequest sendRequest;
    @Transient
    private EmailBody body;
    private String mailFrom;
    private String mailFromName;

    public static EmailSendRequest of(EmailBody body, TargetUpload targetUpload) {
        EmailSendRequest request = new EmailSendRequest();
        request.setEmailBody(body);
        request.setSendRequest(targetUpload.getSendRequest());
        return request;
    }

    private void setSendRequest(SendRequest sendRequest) {
        this.sendRequest = sendRequest;
    }

    private void setEmailBody(EmailBody body) {
        this.body = body;
    }

    protected String resolvedContactByChannel(Map<TargetColumnEnum, String> targetData) {
        return targetData.get(TargetColumnEnum.TARGET_EMAIL);
    }

    protected ContentTypeEnum resolveContentType() {
        return null;
    }
}
