package com.ums.schedule.domain;

import jakarta.persistence.*;
import lombok.Getter;
import org.apache.kafka.common.network.Send;

import java.time.LocalDateTime;

@Entity
public class SendRequestReport {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "report_id")
    @Getter
    private Long id;

    @Column(name = "total_cnt", nullable = false)
    private Long totalCnt;

    @Column(name = "fail_cnt", nullable = false)
    private Long failCnt;

    @Column(name = "made_cnt", nullable = false)
    private Long madeCnt;

    @Column(name = "success_cnt", nullable = false)
    private Long successCnt;

    @Column(name = "download_url")
    private String downloadUrl;

    @Column(name = "aggregated_at", nullable = false)
    private LocalDateTime aggregatedAt;

    @Getter
    @ManyToOne
    @JoinColumn(name = "send_request_id", nullable = false)
    private SendRequest sendRequest;

    public void applySendRequest(SendRequest sendRequest) {
        this.sendRequest = sendRequest;
        this.sendRequest.getSendRequestReportList().add(this);
    }
}
