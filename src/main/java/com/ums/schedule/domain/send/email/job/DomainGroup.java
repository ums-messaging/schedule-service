package com.ums.schedule.domain.send.email.job;

import com.ums.schedule.application.ums.common.send.model.EmailTargetGroupQueryResult;

public record DomainGroup(
        String requestId,
        String uploadId,
        String jobId,
        Long groupId,
        String groupKey,
        Long totalCount,
        String status
) {
    public static DomainGroup of(EmailSendJob emailJob, EmailTargetGroupQueryResult result) {
        SendJob job = emailJob.job();
        return new DomainGroup(
                String.valueOf(job.requestId()),
                job.jobId(),
                job.uploadId().toString(),
                result.groupId(),
                result.emailDomain(),
                result.count(),
                "CREATE"
        );

    }

}
