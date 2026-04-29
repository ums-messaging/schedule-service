package com.ums.schedule.domain.request;

import com.ums.schedule.code.send.*;
import com.ums.schedule.domain.request.event.SendEvent;
import com.ums.schedule.domain.request.exception.InvalidScheduleException;
import com.ums.schedule.domain.request.exception.SendRequestException;
import com.ums.schedule.domain.request.report.SendRequestReport;
import com.ums.schedule.domain.request.state.SendRequestCreateState;
import com.ums.schedule.domain.request.state.SendRequestErrorState;
import com.ums.schedule.domain.request.state.SendRequestState;
import com.ums.schedule.domain.target.event.TargetUploadCompletedEvent;
import com.ums.schedule.domain.target.event.TargetUploadRequestedEvent;
import com.ums.schedule.domain.target.event.TargetUploadUrlCreatedEvent;
import com.ums.schedule.domain.target.exeption.upload.TargetUploadCompleteStateException;
import com.ums.schedule.domain.target.upload.TargetUpload;
import com.ums.schedule.domain.schedule.Schedule;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "send_request",
        uniqueConstraints = {
            @UniqueConstraint(
                    name="uq_customer_request",
                    columnNames = {"customer_id", "customer_request_id"}
            )}
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SendRequest {
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

    @Enumerated(EnumType.STRING)
    private ChannelTypeEnum channelType;

    @Transient
    @Getter
    private SendRequestState state;
    private SendRequestStatusEnum status;

    @Embedded
    private CustomerRequestKey customerRequestKey;

    @OneToOne
    private TargetUpload currentTargetUpload;

    @ManyToOne
    @JoinColumn(name = "schedule_id", nullable = false)
    @Getter
    private Schedule schedule;

    @Getter(AccessLevel.PRIVATE)
    @OneToMany(mappedBy = "sendRequest", cascade = { CascadeType.PERSIST })
    private List<TargetUpload> targetUploadList = new ArrayList<>();

    @ManyToOne
    private SendRequestReport report;

    public static SendRequest of(Schedule schedule, CustomerRequestKey key, ChannelTypeEnum channelType) {
        SendRequest request = new SendRequest();
        request.applySchedule(schedule);
        request.applyCustomerRequestKey(key);
        request.setChannelType(channelType);
        request.changeStatus(new SendRequestCreateState());
        return request;
    }

    private void setChannelType(ChannelTypeEnum channelType) {
        this.channelType = channelType;
    }

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

    private void applySchedule(Schedule schedule) {
        if(!schedule.availableSchedulePeriodAndStatus()) {
            throw InvalidScheduleException.invalidSchedule();
        }
        this.schedule = schedule;
    }

    protected SendRequest onEvent(SendRequestEvent event) {
        SendRequestState toState = this.state.onEvent(event);
        changeStatus(toState);
        return this;
    }

    private void changeStatus(SendRequestState status) {
        this.state = status;
        this.status = state.currentSendRequestStatus();
    }

    public SendRequest onError() {
        changeStatus(new SendRequestErrorState());
        return this;
    }

    public void addTargetUploadList(TargetUpload targetUpload) {
        this.targetUploadList.add(targetUpload);
    }

    public void assignToTargetUpload(TargetUpload targetUpload) {
        this.currentTargetUpload = targetUpload;
    }
}