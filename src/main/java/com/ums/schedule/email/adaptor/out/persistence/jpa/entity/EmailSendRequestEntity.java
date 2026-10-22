package com.ums.schedule.email.adaptor.out.persistence.jpa.entity;

import com.github.f4b6a3.tsid.TsidCreator;
import com.ums.schedule.common.code.email.EmailResultCode;
import com.ums.schedule.email.common.code.EmailSendStatusCode;
import com.ums.schedule.email.domain.common.SendResult;
import com.ums.schedule.email.domain.requests.EmailSendRequest;
import com.ums.schedule.email.domain.requests.id.RequestId;
import com.ums.schedule.email.domain.requests.result.SendRequestReport;
import com.ums.schedule.email.domain.requests.result.SendResultReport;
import com.ums.schedule.email.domain.requests.sender.Sender;
import com.ums.schedule.email.domain.requests.state.EmailSendRequestState;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Getter
@Entity
@Table(name = "email_send_request")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EmailSendRequestEntity extends BaseEntity {
    @Id
    private Long id;

    private Integer totalCount;
    @Column(name = "group_count")
    private int groupCount;
    private int requestSuccessCount;
    private int requestFailCount;
    private int sendSuccessCount;
    private int sendFailCount;

    private String status;
    private String resultCode;
    private String resultMessage;

    @Column(name = "sender")
    private String from;
    @Column(name = "sender_name")
    private String fromName;

    @Column(name = "request_id", nullable = false)
    private Long requestId;

    @ManyToOne
    @JoinColumn(name = "messageId")
    private EmailSendMessageEntity message;

    private LocalDateTime sendStartedAt;
    private LocalDateTime sendEndedAt;

    public static EmailSendRequestEntity fromDomain(EmailSendRequest domain) {
        EmailSendRequestEntity entity = new EmailSendRequestEntity(domain);
        entity.assignFrom(domain.getSender());
        entity.assignSendReport(domain.getGroupCount(), domain.getSendRequestReport());
        entity.assignSendResultReport(domain.getSendResultReport());
        entity.assignSendResult(domain.getResult());
        return entity;
    }

    private void assignSendResultReport(SendResultReport sendResultReport) {
        if(sendResultReport != null) {
            this.sendSuccessCount = sendResultReport.successCount();
            this.sendFailCount = sendResultReport.failCount();
        }
    }

    protected EmailSendRequestEntity(EmailSendRequest domain) {
        this.id = generateId(domain.getId());
        this.requestId = domain.getRequestId();
        this.status = domain.getState().name();
        this.createdAt = domain.getCreatedAt();
    }

    private void assignSendResult(SendResult result) {
        if(result != null) {
            this.resultCode = result.resultCode().code();
            this.resultMessage = result.resultMessage();
        }
    }

    private void assignStatus(EmailSendRequestState state) {
        this.status = state.currentStateCode().name();
    }

    private void assignSendReport(Integer groupCount, SendRequestReport report) {
        this.groupCount = groupCount;
        this.totalCount = report.totalCount();
        this.requestSuccessCount = (int) report.successCount();
        this.requestFailCount = (int) report.failCount();
    }

    private void assignFrom(Sender sender) {
        if(sender != null) {
            this.from = sender.from();
            this.fromName = sender.fromName();
        }
    }

    private Long generateId(Long id) {
        if(id == null) {
            return TsidCreator.getTsid().toLong();
        }
        return id;
    }

    private void assignRefIds(RequestId id) {
        this.requestId = id.requestId();
    }

    public void assignSendMessage(EmailSendMessageEntity message) {
        this.message = message;
    }

    public EmailSendRequest toDomain() {
        return EmailSendRequest.fromEntity(this);
    }

    public Sender getSender() {
        return new Sender(from, fromName);
    }

    public SendResult getResult() {
        if(this.resultCode == null) {
            return null;
        }
        return new SendResult(EmailResultCode.valueOf(resultCode), resultMessage);
    }

    public void updateStatus(EmailSendRequest sendRequest) {
        this.status = sendRequest.getState().name();
        this.sendStartedAt = sendRequest.getSendStartedAt();
        this.sendEndedAt = sendRequest.getSendEndedAt();
    }

    public void updateSendRequestReport(SendRequestReport report) {
        this.totalCount = report.totalCount();
        this.groupCount = report.groupCount();
        this.requestSuccessCount = (int) report.successCount();
        this.requestFailCount = (int) report.failCount();

    }

    public SendRequestReport getSendRequestReport() {
        return new SendRequestReport(totalCount, groupCount, requestSuccessCount, requestFailCount);
    }

    public SendResultReport getSendResultReport() {
        if(this.status.equals(EmailSendStatusCode.COMPLETED.name())) {
           return new SendResultReport(this.sendSuccessCount, this.sendFailCount);
        }
        return null;
    }
}
