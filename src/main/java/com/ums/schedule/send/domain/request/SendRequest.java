package com.ums.schedule.send.domain.request;

import com.ums.schedule.common.code.EnumMapperValue;
import com.ums.schedule.message.domain.SendMessage;
import com.ums.schedule.schedule.domain.Schedule;
import com.ums.schedule.send.application.model.dto.SendRequestDto;
import com.ums.schedule.send.code.SendRequestStatusEnum;
import com.ums.schedule.send.code.TargetUploadStatusEnum;
import com.ums.schedule.send.domain.exception.SendRequestException;
import com.ums.schedule.send.domain.reporing.SendReport;
import com.ums.schedule.send.domain.request.status.RequestCreateStatus;
import com.ums.schedule.send.domain.request.status.SchedulingStatus;
import com.ums.schedule.send.domain.request.status.SendRequestStatus;
import com.ums.schedule.send.domain.request.status.exception.SendReadyStatusException;
import com.ums.schedule.send.domain.target.SendTarget;
import com.ums.schedule.send.domain.target.upload.TargetUpload;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import static com.ums.schedule.send.code.TargetUploadStatusEnum.*;

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

//    @Column(name = "status", nullable = false)

    private CustomerRequestKey customerRequestKey;
    private SendReport report;
    //    @ManyToOne
//    @JoinColumn(name = "schedule_id", nullable = false)
//    @Getter
    private Schedule schedule;
    //    @Getter
//    @OneToMany(mappedBy = "sendRequest", cascade = { CascadeType.PERSIST })
    private SendMessage sendMessage;
    private List<TargetUpload> targetUploadList = new ArrayList<>();
    private List<SendTarget> targetList = new ArrayList<>();

    private String errorMessage;
    private SendRequestEvent event;


    public static SendRequest of(CustomerRequestKey customerRequestKey, SendRequestDto dto) {
        SendRequest sendRequest = new SendRequest();
        sendRequest.applyCustomerRequestKey(customerRequestKey);
        sendRequest.fromDto(dto);
        sendRequest.markCreate();
        return sendRequest;
    }

    private void markCreate() {
        this.event = new SendRequestEvent();
    }

    private void initReport(int totalCount) {
        this.report = SendReport.of(totalCount);
    }

    private void applyCustomerRequestKey(CustomerRequestKey customerRequestKey) {
        this.customerRequestKey = customerRequestKey;
    }

    private void fromDto(SendRequestDto dto) {
        this.senderKey = dto.senderKey();
        this.templateKey = dto.templateKey();
        initRetryMaxCount(dto.retryCnt());
        initReport(dto.totalCount());
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
        this.event.markMessageCreated(this);
    }
}
