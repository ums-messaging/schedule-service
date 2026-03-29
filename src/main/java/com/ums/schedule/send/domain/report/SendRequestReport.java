package com.ums.schedule.send.domain.report;

import com.ums.schedule.send.domain.request.SendRequest;
import com.ums.schedule.send.domain.target.SendTarget;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

//@Entity
@NoArgsConstructor
public class SendRequestReport {
//    @Id
//    @GeneratedValue(strategy = GenerationType.SEQUENCE)
//    @Column(name = "report_id")
//    @Getter
    private Long id;

//    @Column(name = "total_count", nullable = false)
    private Long totalCount;

//    @Column(name = "fail_count", nullable = false)
    private Long failCount;

//    @Column(name = "sending_count", nullable = false)
    private Long sendingCount;

//    @Column(name = "success_count", nullable = false)
    private Long successCount;

//    @Column(name = "download_url")
    private String downloadUrl;

//    @Column(name = "aggregated_at", nullable = false)
    private LocalDateTime aggregatedAt;

//    @Getter
//    @ManyToOne
//    @JoinColumn(name = "send_request_id", nullable = false)
    private SendRequest sendRequest;

    public static SendRequestReport of(SendRequest sendRequest) {
        SendRequestReport report = new SendRequestReport();
        report.applySendRequest(sendRequest);
        return report;
    }

    private void applySendRequest(SendRequest sendRequest) {
        this.sendRequest = sendRequest;
        this.totalCount = countTargetList(sendRequest.getTargetList());
    }

    private Long countTargetList(List<SendTarget> targetList) {
        return Long.parseLong(String.valueOf(targetList.size()));
    }
}
