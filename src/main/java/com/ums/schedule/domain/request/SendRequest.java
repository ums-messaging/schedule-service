package com.ums.schedule.domain.request;

import com.ums.schedule.application.request.dto.SendRequestCommand;
import com.ums.schedule.code.EnumMapperValue;
import com.ums.schedule.code.send.*;
import com.ums.schedule.domain.request.exception.InvalidScheduleException;
import com.ums.schedule.domain.request.state.SendRequestCreateState;
import com.ums.schedule.domain.request.state.SendRequestErrorState;
import com.ums.schedule.domain.request.state.SendRequestState;
import com.ums.schedule.domain.target.upload.TargetUpload;
import com.ums.schedule.domain.schedule.Schedule;
import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class SendRequest {
    @Id
    @Tsid
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

    @Column(name = "channel_type", nullable = false)
    @Convert(converter = ChanelTypeConverter.class)
    private ChannelTypeEnum channelType;

    @Transient
    @Getter
    private SendRequestState state;

    @Column(name = "status", nullable = false, columnDefinition = "varchar(10) default 'CREATE'")
    private SendRequestStatusEnum status;

    @Embedded
    private CustomerRequestKey customerRequestKey;

    @JoinColumn(name = "upload_id")
    @OneToOne(fetch = FetchType.LAZY)
    private TargetUpload currentTargetUpload;

    @Getter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_id", nullable = false)
    private Schedule schedule;

    @OneToMany(mappedBy = "sendRequest")
    private List<TargetUpload> targetUploadList = new ArrayList<>();

    public static SendRequest of(Schedule schedule, SendRequestCommand command) {
        SendRequest request = new SendRequest();
        request.applySchedule(schedule);
        request.applyCustomerRequestKey(command.customerId(), command.customerKey(), command.existsKey());
        request.setChannelType(command.channel());
        request.changeStatus(new SendRequestCreateState());
        return request;
    }

    private void setChannelType(EnumMapperValue channelType) {
        this.channelType = ChannelTypeEnum.valueOf(channelType.code());
    }

    public void applyCustomerRequestKey(String customerId, String customerKey, boolean exists) {
        this.customerRequestKey = CustomerRequestKey.of(customerId, customerKey, exists);
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
        if(targetUpload.getUploadId() == null) {
            targetUpload.applySendRequest(this);
            this.targetUploadList.add(targetUpload);
        }
    }

    @PrePersist
    public void prePersist() {
        if(state == null) {
            changeStatus(new SendRequestCreateState());
        }
        this.retryCnt = Optional.ofNullable(this.retryCnt).orElse(3);
    }
}