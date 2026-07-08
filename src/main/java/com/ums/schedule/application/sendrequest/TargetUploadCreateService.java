package com.ums.schedule.application.sendrequest;

import com.ums.schedule.application.sendrequest.command.TargetUploadCreateCommand;
import com.ums.schedule.application.sendrequest.context.TargetUploadReportCreateContext;
import com.ums.schedule.application.sendrequest.target.result.FileTargetUploadResult;
import com.ums.schedule.application.sendrequest.target.result.TargetUploadResult;
import com.ums.schedule.common.code.mapper.EnumMapperFactory;
import com.ums.schedule.common.code.mapper.EnumMapperValue;
import com.ums.schedule.config.properties.TargetUploadProperties;
import com.ums.schedule.domain.sendrequest.SendRequest;
import com.ums.schedule.domain.sendrequest.target.upload.TargetUploadReport;
import com.ums.schedule.domain.sendrequest.target.upload.TargetUploadReportJpaRepository;
import com.ums.schedule.domain.sendrequest.target.upload.code.TargetUploadReportEnumMapper;
import com.ums.schedule.domain.sendrequest.target.upload.code.TargetUploadTypeEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TargetUploadCreateService  {
    private final EnumMapperFactory factory;
    private final TargetUploadProperties properties;
    private final FileTargetUploadUrlProvider provider;
    private final TargetUploadReportJpaRepository jpaRepository;

    @Transactional
    public TargetUploadResult create(SendRequest sendRequest, TargetUploadCreateCommand command) {
        TargetUploadReport targetUploadReport = createTargetUploadReport(sendRequest, command);
        FileTargetUploadResult result = provideUploadUrl(targetUploadReport);

        jpaRepository.save(targetUploadReport);

        return TargetUploadResult.of(targetUploadReport, result);
    }

    private FileTargetUploadResult provideUploadUrl(TargetUploadReport report) {
        FileTargetUploadResult result = Optional.ofNullable(report.getUploadType())
                .filter(type -> TargetUploadTypeEnum.FILE == type)
                .map(type -> provider.provide(report.getUploadKey()))
                .orElse(null);
        return result;
    }

    private TargetUploadReport createTargetUploadReport(SendRequest sendRequest, TargetUploadCreateCommand command) {
        EnumMapperValue uploadFormat = resolveUploadFormat(command.uploadFormat());
        TargetUploadReportCreateContext context = command.toContext(sendRequest, uploadFormat, properties);
        return TargetUploadReport.of(context);
    }

    private EnumMapperValue resolveUploadFormat(String uploadFormat) {
        return factory.findEnumMapperValue(TargetUploadReportEnumMapper.UPLOAD_FORMAT, uploadFormat);
    }
}
