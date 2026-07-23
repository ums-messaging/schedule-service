package com.ums.schedule.application.target.upload;

import com.ums.schedule.application.target.upload.handler.FileTargetUploadUrlProvider;
import com.ums.schedule.application.sendrequest.command.TargetUploadCreateCommand;
import com.ums.schedule.application.target.upload.exception.TargetUploadReportNotConfiguredException;
import com.ums.schedule.application.target.upload.model.TargetUploadReportCreateContext;
import com.ums.schedule.application.target.upload.handler.FileTargetUploadResult;
import com.ums.schedule.application.target.upload.model.TargetUploadResult;
import com.ums.schedule.common.code.mapper.EnumMapperFactory;
import com.ums.schedule.common.code.mapper.EnumMapperValue;
import com.ums.schedule.common.code.target_upload.TargetUploadUploadPrefix;
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
                .filter(type -> TargetUploadType.FILE == type)
                .map(type -> provider.provide(report.getUploadKey()))
                .orElse(null);
        return result;
    }

    private TargetUploadReport createTargetUploadReport(SendRequest sendRequest, TargetUploadCreateCommand command) {
        EnumMapperValue uploadFormat = resolveUploadFormat(command.uploadFormat());
        Map<TargetUploadUploadPrefix, String> uploadDirMap = generateUploadDir(sendRequest.generateRequestUploadDir(), properties);
        TargetUploadReportCreateContext context = command.toContext(sendRequest, uploadDirMap, uploadFormat);
        return TargetUploadReport.of(context);
    }

    private Map<TargetUploadUploadPrefix, String> generateUploadDir(String uploadDir, TargetUploadProperties properties) {
        Map<TargetUploadUploadPrefix, String> uploadMap = new EnumMap<>(TargetUploadUploadPrefix.class);

        putUploadDirMap(uploadMap, TargetUploadUploadPrefix.UPLOAD_KEY, uploadDir, properties.getUploadKey());
        putUploadDirMap(uploadMap, TargetUploadUploadPrefix.DOWNLOAD_KEY, uploadDir, properties.getDownloadKey());
        return uploadMap;
    }

    private void putUploadDirMap(Map<TargetUploadUploadPrefix, String> uploadMap, TargetUploadUploadPrefix prefix, String uploadDir, String fileKeyPrefix) {
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
