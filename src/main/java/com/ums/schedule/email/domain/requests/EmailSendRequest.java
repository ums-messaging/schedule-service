package com.ums.schedule.email.domain.requests;

import com.ums.schedule.email.adaptor.out.persistence.jpa.entity.EmailSendRequestEntity;
import com.ums.schedule.email.application.requests.command.SendRequestCommand;
import com.ums.schedule.email.application.target.reader.model.SendTargetsReadResult;
import com.ums.schedule.email.common.code.EmailSendStatusCode;
import com.ums.schedule.email.domain.common.SendResult;
import com.ums.schedule.email.domain.requests.result.SendRequestReport;
import com.ums.schedule.email.domain.requests.result.SendResultReport;
import com.ums.schedule.email.domain.requests.sender.Sender;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
public class EmailSendRequest {
    private Long id;
    private UUID messageId;
    private Long requestId;

    private EmailSendStatusCode state;

    private Integer groupCount;
    private SendRequestReport sendRequestReport;
    private SendResultReport sendResultReport;
    private SendResult result;
    private Sender sender;

    private LocalDateTime createdAt;
    private LocalDateTime sendRequestedAt;
    private LocalDateTime sendStartedAt;
    private LocalDateTime sendEndedAt;
    private LocalDateTime lastUpdatedAt;

    public static EmailSendRequest of(SendRequestCommand command) {
        return new EmailSendRequest(command);
    }

    private EmailSendRequest(SendRequestCommand command) {
        this.requestId = command.requestId();
        this.messageId = command.messageId();
        this.state = EmailSendStatusCode.CREATE;
        this.sender = command.sender();
        this.groupCount = 0;
        this.sendRequestReport = SendRequestReport.initialize();
    }

    public EmailSendRequestEntity toEntity() {
        return EmailSendRequestEntity.fromDomain(this);
    }
    public static EmailSendRequest fromEntity(EmailSendRequestEntity entity) {
        return new EmailSendRequest(entity);
    }

    private EmailSendRequest(EmailSendRequestEntity entity) {
        this.id = entity.getId();
        this.requestId = entity.getRequestId();
        this.state = EmailSendStatusCode.valueOf(entity.getStatus());
        this.messageId = entity.getMessage().getId();
        this.sender = entity.getSender();
        this.groupCount = entity.getGroupCount();
        this.sendRequestReport = entity.getSendRequestReport();
        this.sendResultReport = entity.getSendResultReport();
        this.result = entity.getResult();
        this.createdAt = entity.getCreatedAt();
        this.sendRequestedAt = entity.getSendRequestedAt();
        this.sendStartedAt = entity.getSendStartedAt();
        this.sendEndedAt = entity.getSendEndedAt();
    }

    public void startSend() {
        this.sendStartedAt = LocalDateTime.now();
        this.state = EmailSendStatusCode.SENDING;
    }

    public void endSend(SendResult result, SendResultReport sendResultReport) {
        this.sendEndedAt = LocalDateTime.now();
        this.state = EmailSendStatusCode.COMPLETED;
        this.result = result;
        this.sendResultReport = sendResultReport;
    }

    public SendRequestReport uploadRequest(SendTargetsReadResult result, Long failCount) {
        SendRequestReport sendRequestReport = SendRequestReport.of(result, failCount);
        this.groupCount = result.groupCount();
        this.sendRequestReport = sendRequestReport;
        this.sendRequestedAt = LocalDateTime.now();
        return sendRequestReport;
    }
}
