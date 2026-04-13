package com.ums.schedule.domain.request;

import com.ums.schedule.code.send.ContentTypeEnum;
import com.ums.schedule.code.send.SendRequestStatusEnum;
import com.ums.schedule.code.send.TargetColumnEnum;
import com.ums.schedule.domain.request.event.SendRequestEvent;
import com.ums.schedule.domain.request.report.SendRequestReport;
import com.ums.schedule.domain.request.status.RequestCreateState;
import com.ums.schedule.domain.request.status.SendRequestState;
import com.ums.schedule.domain.target.upload.TargetUpload;
import com.ums.schedule.domain.schedule.Schedule;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "send_request",
        uniqueConstraints =
        {@UniqueConstraint(name="uq_customer_request", columnNames = {"customer_id", "customer_request_id"})}
)
@Getter
public abstract class SendRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "send_request_id")
    @Getter
    private Long id;

    @Column(name = "template_key", nullable = false)
    private String templateKey;

    @Column(name = "retry_cnt", nullable = false)
    private Integer retryCnt;

    @Column(name = "sender_key", nullable = false)
    private String senderKey;

    @Transient
    private SendRequestState state;
    private SendRequestStatusEnum status;

    @Embedded
    private CustomerRequestKey customerRequestKey;

    @ManyToOne
    @JoinColumn(name = "schedule_id", nullable = false)
    @Getter
    private Schedule schedule;

    @Getter
    @OneToMany(mappedBy = "sendRequest", cascade = { CascadeType.PERSIST })
    private List<TargetUpload> targetUploadList = new ArrayList<>();

    @Getter(AccessLevel.PRIVATE)
    @OneToMany(mappedBy = "sendRequest")
    private List<SendRequestEvent> eventList = new ArrayList<>();

    @ManyToOne
    private SendRequestReport report;

    public void applyCustomerRequestKey(CustomerRequestKey customerRequestKey) {
        this.customerRequestKey = customerRequestKey;
    }

    public void setSenderAndTemplateKey(String senderKey, String templateKey) {
        this.senderKey = senderKey;
        this.templateKey = templateKey;
    }

    public void initRetryMaxCount(Integer retryCount) {
        this.retryCnt = retryCount;
    }
    public void applySchedule(Schedule schedule) {
        this.schedule = schedule;
        this.schedule.getSendRequests().add(this);
    }

//    public boolean canTransitionToReady() {
//        return isMessageCreated() && isTargetUploaded();
//    }

//    private boolean isMessageCreated() {
//        return Optional.ofNullable(this.sendMessage)
//                .filter(message -> message.getStatus() == MessageStatusEnum.ACTIVE)
//                .map(message -> true)
//                .orElse(false);
//    }

    protected SendRequest() {
        this.state = new RequestCreateState();
    }

    private boolean isTargetUploaded() {
        if(this.targetUploadList.size()>0) {
            return this.targetUploadList
                    .stream()
                    .filter(upload -> upload.isProcess())
                    .findAny()
                    .map(upload -> false)
                    .orElse(true);
        }
        return false;
    }

    public void addEventList(SendRequestEvent event) {
        changeStatus(event);
        this.eventList.add(event);
    }

    private void changeStatus(SendRequestEvent event) {
        this.state = event.mark(this.state);
        this.status = state.currentSendRequestStatus();
    }


    public ContentTypeEnum getContentType() {
        return resolveContentType();
    }

    public String getContact(Map<TargetColumnEnum, String> targetData) {
        return resolvedContactByChannel(targetData);
    }

    protected abstract String resolvedContactByChannel(Map<TargetColumnEnum, String> targetData);
    protected abstract ContentTypeEnum resolveContentType();

}
