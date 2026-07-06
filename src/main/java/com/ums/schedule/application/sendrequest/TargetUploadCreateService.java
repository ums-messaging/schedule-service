package com.ums.schedule.application.sendrequest;

import com.ums.schedule.application.sendrequest.command.TargetUploadCreateCommand;
import com.ums.schedule.application.sendrequest.target.result.FileTargetUploadResult;
import com.ums.schedule.application.sendrequest.target.result.TargetUploadResult;
import com.ums.schedule.config.properties.TargetUploadProperties;
import com.ums.schedule.domain.sendrequest.SendRequest;
import com.ums.schedule.domain.sendrequest.target.upload.TargetUploadReport;
import com.ums.schedule.domain.sendrequest.target.upload.TargetUploadReportJpaRepository;
import com.ums.schedule.domain.sendrequest.target.upload.code.TargetUploadTypeEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TargetUploadCreateService  {
    private final TargetUploadProperties properties;
    private final FileTargetUploadUrlProvider provider;
    private final TargetUploadReportJpaRepository jpaRepository;

    @Transactional
    public TargetUploadResult create(SendRequest sendRequest, TargetUploadCreateCommand command) {
        TargetUploadReport targetUploadReport = TargetUploadReport.of(sendRequest, command, properties.getDownloadKey());
        FileTargetUploadResult result = Optional.ofNullable(command.uploadType())
                .filter(type -> TargetUploadTypeEnum.FILE == type)
                .map(type -> provider.provide(targetUploadReport, command.uploadFormat(), properties.getUploadKey()))
                .orElse(null);

        jpaRepository.save(targetUploadReport);

        return TargetUploadResult.of(targetUploadReport, result);
    }
}
