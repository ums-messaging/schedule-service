package com.ums.schedule.application.sendrequest;

import com.ums.schedule.adapter.storage.AwsS3Repository;
import com.ums.schedule.application.sendrequest.command.TargetUploadCreateCommand;
import com.ums.schedule.application.sendrequest.target.result.TargetUploadResult;
import com.ums.schedule.config.properties.TargetUploadProperties;
import com.ums.schedule.domain.sendrequest.SendRequest;
import com.ums.schedule.domain.sendrequest.target.upload.TargetUploadReport;
import com.ums.schedule.domain.sendrequest.target.upload.TargetUploadReportJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TargetUploadCreateService {
    private final TargetUploadProperties properties;
    private final TargetUploadReportJpaRepository jpaRepository;
    private final AwsS3Repository fileRepository;

    public TargetUploadResult create(SendRequest sendRequest, TargetUploadCreateCommand command) {
        TargetUploadReport report = TargetUploadReport.of(sendRequest, command, properties.getUploadKey());
        return TargetUploadResult.of(report);
    }

}
