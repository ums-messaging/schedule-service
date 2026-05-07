package com.ums.schedule.domain.channel.email;

import com.ums.schedule.code.send.ContentTypeEnum;
import com.ums.schedule.code.send.TargetColumnEnum;
import com.ums.schedule.domain.channel.email.message.EmailBody;
import com.ums.schedule.domain.request.SendRequest;
import com.ums.schedule.domain.target.upload.TargetUpload;
import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Map;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class EmailSendRequest {
    @Id
    @Tsid
    private Long id;

    @MapsId
    @JoinColumn(name = "request_id")
    @OneToOne(fetch = FetchType.LAZY)
    private SendRequest sendRequest;

    @Embedded
    private EmailBody body;
    @Column(name = "mail_from", nullable = false)
    private String mailFrom;
    @Column(name = "mail_from_name", nullable = false)
    private String mailFromName;
    @Column(name = "email_template_key", nullable = false)
    private String emailTemplateKey;

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

    public void applyTemplateKey(String key) {
        this.emailTemplateKey = key;
    }

    public void applyMailFrom(String mailFrom, String mailFromName) {
        this.mailFrom = mailFrom;
        this.mailFromName = mailFromName;
    }
}
