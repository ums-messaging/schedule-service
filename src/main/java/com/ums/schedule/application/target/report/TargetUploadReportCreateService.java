package com.ums.schedule.application.target.report;

import com.ums.schedule.application.target.provider.FileTargetUploadUrlProvider;
import com.ums.schedule.application.sendrequest.command.TargetUploadCreateCommand;
import com.ums.schedule.application.target.exception.TargetUploadReportNotConfiguredException;
import com.ums.schedule.application.target.report.model.TargetUploadReportCreateContext;
import com.ums.schedule.application.target.provider.FileTargetUploadResult;
import com.ums.schedule.application.target.report.model.TargetUploadResult;
import com.ums.schedule.common.code.mapper.EnumMapperFactory;
import com.ums.schedule.common.code.mapper.EnumMapperValue;
import com.ums.schedule.common.code.target_upload.TargetUploadConfiguration;
import com.ums.schedule.config.properties.TargetUploadProperties;
import com.ums.schedule.domain.request.SendRequest;
import com.ums.schedule.domain.target.upload.TargetUploadReport;
import com.ums.schedule.domain.target.upload.TargetUploadReportJpaRepository;
import com.ums.schedule.common.code.target_upload.TargetUploadReportCode;
import com.ums.schedule.common.code.target_upload.TargetUploadType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TargetUploadReportCreateService {
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
                .filter(type -> TargetUploadType.FILE == type)
                .map(type -> provider.provide(report.getUploadKey()))
                .orElse(null);
        return result;
    }

    private TargetUploadReport createTargetUploadReport(SendRequest sendRequest, TargetUploadCreateCommand command) {
        EnumMapperValue uploadFormat = resolveUploadFormat(command.uploadFormat());
        Map<TargetUploadConfiguration, String> uploadDirMap = generateUploadDir(sendRequest.generateRequestUploadDir(), properties);
        TargetUploadReportCreateContext context = command.toContext(sendRequest, uploadDirMap, uploadFormat);
        return TargetUploadReport.of(context);
    }

    private Map<TargetUploadConfiguration, String> generateUploadDir(String uploadDir, TargetUploadProperties properties) {
        Map<TargetUploadConfiguration, String> uploadMap = new EnumMap<>(TargetUploadConfiguration.class);

        putUploadDirMap(uploadMap, TargetUploadConfiguration.UPLOAD_KEY, uploadDir, properties.getPrefixUploadKey());
        putUploadDirMap(uploadMap, TargetUploadConfiguration.DOWNLOAD_KEY, uploadDir, properties.getPrefixDownloadKey());
        return uploadMap;
    }

    private void putUploadDirMap(Map<TargetUploadConfiguration, String> uploadMap, TargetUploadConfiguration prefix, String uploadDir, String fileKeyPrefix) {
        String fileKey = "%s/%s".formatted(fileKeyPrefix, uploadDir);

        if(!StringUtils.hasText(fileKeyPrefix)) {
            throw TargetUploadReportNotConfiguredException.of(prefix);
        }
        uploadMap.put(prefix, fileKey);
    }


    private EnumMapperValue resolveUploadFormat(String uploadFormat) {
        return factory.findEnumMapperValue(TargetUploadReportCode.UPLOAD_FORMAT, uploadFormat);
    }
}
