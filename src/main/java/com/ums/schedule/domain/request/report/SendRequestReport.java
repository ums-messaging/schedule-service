package com.ums.schedule.domain.request.report;

import com.ums.schedule.domain.request.SendRequest;
import com.ums.schedule.domain.target.upload.TargetUpload;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.LongStream;

@Entity
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

    @Getter
    @ManyToOne
    @JoinColumn(name = "send_request_id", nullable = false)
    private SendRequest sendRequest;

    public static SendRequestReport of(SendRequest sendRequest) {
        SendRequestReport report = new SendRequestReport();
        report.applySendRequest(sendRequest);
        return report;
    }

    private void applySendRequest(SendRequest sendRequest) {
        this.sendRequest = sendRequest;
        this.totalCount = countTargetList(sendRequest.getTargetUploadList());
    }

    private Long countTargetList(List<TargetUpload> targetUploadList) {
        return targetUploadList.stream()
                .map(upload -> upload.getTargetList().size())
                .flatMapToLong(integer -> LongStream.of(integer.longValue()))
                .sum();
    }
}
