package com.ums.schedule.domain;

import com.ums.schedule.common.code.*;
import jakarta.persistence.*;
import lombok.Getter;
import org.apache.kafka.common.network.Send;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "send_request",
        uniqueConstraints =
        {@UniqueConstraint(name="uq_customer_request", columnNames = {"customer_id", "customer_request_id"})}
)
public class SendRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "send_request_id")
    @Getter
    private Long id;

    @Column(name = "template_id", nullable = false)
    private String templateId;

    @Enumerated(EnumType.STRING)
    @Column(name = "channel", nullable = false)
    private ChannelEnum channel;

    @Column(name = "retry_cnt", nullable = false)
    private Integer retryCnt;

    @Column(name = "attempt_order_no", nullable = false)
    private Integer attemptOrderNo;

    @Column(name = "sender", nullable = false)
    private String sender;

    @Column(name = "requested_at", nullable = false)
    private LocalDateTime requestedAt;

    @Column(name = "status", nullable = false)
    private SendRequestStatusEnum status;

    @Column(name = "send_type", nullable = false)
    private SendTypeEnum sendType;

    @Column(name = "message_type", nullable = false)
    private MessageTypeEnum messageType;

    @Column(name = "resume_yn",nullable = false)
    @Enumerated(EnumType.STRING)
    private YesOrNoEnum resumeYn;

    @Column(name = "bypass_yn", nullable = false)
    @Enumerated(EnumType.STRING)
    private YesOrNoEnum bypassYn;

    @Column(name = "customer_request_id", nullable = false)
    private String customerRequestId;

    @Column(name = "customer_id", nullable = false)
    private String customerId;

    @ManyToOne
    @JoinColumn(name = "schedule_id", nullable = false)
    @Getter
    private Schedule schedule;

    @Getter
    @OneToMany(mappedBy = "sendRequest", cascade = { CascadeType.PERSIST })
    private List<SendMessage> sendMessages = new ArrayList<>();

    @Getter
    @OneToMany(mappedBy = "sendRequest", cascade = { CascadeType.PERSIST} )
    private List<SendRequestReport> sendRequestReportList = new ArrayList<>();

//    @Getter
//    @OneToMany(mappedBy = "parentSendRequest", cascade = { CascadeType.PERSIST })
//    private List<SendRequest> pChildSendRequestList = new ArrayList<>();
//
//    @Getter
//    @ManyToOne
//    @JoinColumn(name = "parent_send_request_id")
//    private SendRequest parentSendRequest;
//
//    @Getter
//    @OneToMany(mappedBy = "rootSendRequest")
//    private List<SendRequest> rChildSendReuqestList = new ArrayList<>();
//
//    @ManyToOne
//    @JoinColumn(name = "root_send_request_id")
//    private SendRequest rootSendRequest;


    public void applySchedule(Schedule schedule) {
        this.schedule = schedule;
        this.schedule.getSendRequests().add(this);
    }

//    public void applyParentSendRequest(SendRequest parentSendRequest) {
//        this.parentSendRequest = parentSendRequest;
//        parentSendRequest.getPChildSendRequestList().add(this);
//        applySchedule(parentSendRequest.getSchedule());
//    }
//
//    public void applyRootSendRequest(SendRequest rootSendRequest) {
//        this.rootSendRequest = rootSendRequest;
//        rootSendRequest.getRChildSendReuqestList().add(this);
//    }
}
