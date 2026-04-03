package com.ums.schedule.send.domain.request;

import com.ums.schedule.message.code.MessageStatusEnum;
import com.ums.schedule.send.code.SendRequestStatusEnum;
import com.ums.schedule.send.domain.event.SendRequestEvent;
import com.ums.schedule.send.domain.report.SendRequestReport;
import com.ums.schedule.message.domain.SendMessage;
import com.ums.schedule.schedule.domain.Schedule;
import com.ums.schedule.send.application.model.dto.SendRequestDto;
import com.ums.schedule.send.domain.request.status.SendRequestState;
import com.ums.schedule.send.domain.target.SendTarget;
import com.ums.schedule.send.domain.request.upload.TargetUpload;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

//@Entity
//@Table(name = "send_request",
//        uniqueConstraints =
//        {@UniqueConstraint(name="uq_customer_request", columnNames = {"customer_id", "customer_request_id"})}
//)
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SendRequest {
//    @Id
//    @GeneratedValue(strategy = GenerationType.SEQUENCE)
//    @Column(name = "send_request_id")
    @Getter
    private Long id;

//    @Column(name = "template_key", nullable = false)
    private String templateKey;

//    @Column(name = "retry_cnt", nullable = false)
    private Integer retryCnt;

//    @Column(name = "sender_key", nullable = false)
    private String senderKey;

    private SendRequestState state;
    private SendRequestStatusEnum status;

    private CustomerRequestKey customerRequestKey;

    //    @ManyToOne
//    @JoinColumn(name = "schedule_id", nullable = false)
//    @Getter
    private Schedule schedule;
    //    @Getter
//    @OneToMany(mappedBy = "sendRequest", cascade = { CascadeType.PERSIST })
    private List<TargetUpload> targetUploadList = new ArrayList<>();

    private List<SendTarget> targetList = new ArrayList<>();

    @Getter(AccessLevel.PRIVATE)
    private List<SendRequestEvent> eventList = new ArrayList<>();

    private SendRequestReport report ;
    private SendMessage sendMessage;

    public static SendRequest of(CustomerRequestKey customerRequestKey, SendRequestDto dto) {
        SendRequest sendRequest = new SendRequest();
        sendRequest.applyCustomerRequestKey(customerRequestKey);
        sendRequest.fromDto(dto);
        sendRequest.createReport();
        return sendRequest;
    }

    private void createReport() {
        this.report = SendRequestReport.of(this);
    }

    private void applyCustomerRequestKey(CustomerRequestKey customerRequestKey) {
        this.customerRequestKey = customerRequestKey;
    }

    private void fromDto(SendRequestDto dto) {
        this.senderKey = dto.senderKey();
        this.templateKey = dto.templateKey();
        initRetryMaxCount(dto.retryCnt());
    }

    private void initRetryMaxCount(Integer retryCount) {
        this.retryCnt = retryCount;
    }

    public void applySchedule(Schedule schedule) {
        this.schedule = schedule;
        this.schedule.getSendRequests().add(this);
    }

    public void applySendMessage(SendMessage sendMessage) {
        this.sendMessage = sendMessage;
        sendMessage.getSendRequest().add(this);
    }

    public boolean canTransitionToReady() {
        return isMessageCreated() && isTargetUploaded();
    }

    private boolean isMessageCreated() {
        return Optional.ofNullable(this.sendMessage)
                .filter(message -> message.getStatus() == MessageStatusEnum.ACTIVE)
                .map(message -> true)
                .orElse(false);
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

}
