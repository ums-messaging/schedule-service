package com.ums.schedule.domain.send.report;

import com.ums.schedule.domain.request.SendRequest;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class SendRequestReport {
    @Id
//    @GeneratedValue(strategy = GenerationType.SEQUENCE)
//    @Column(name = "report_id")
//    @Getter
    private Long id;

    @Column(name = "total_count", nullable = false)
    private Long totalCount;

    @Column(name = "fail_count", nullable = false)
    private Long failCount;

    @Column(name = "sending_count", nullable = false)
    private Long sendingCount;

    @Column(name = "success_count", nullable = false)
    private Long successCount;

    @Column(name = "download_url")
    private String downloadUrl;

//    @Column(name = "aggregated_at", nullable = false)
    private LocalDateTime aggregatedAt;

    @Getter
    @OneToOne
    @JoinColumn(name = "send_request_id", nullable = false)
    private SendRequest sendRequest;

    public static SendRequestReport of(SendRequest sendRequest, Long totalCount) {
        SendRequestReport report = new SendRequestReport();
        report.assignSendRequest(sendRequest);
        report.assignTotalCount(totalCount);
        return report;
    }

    private void assignTotalCount(Long totalCount) {
        this.totalCount = totalCount;
        this.successCount = 0L;
        this.failCount = 0L;
    }

    private void assignSendRequest(SendRequest sendRequest) {
        this.sendRequest = sendRequest;
    }

}
