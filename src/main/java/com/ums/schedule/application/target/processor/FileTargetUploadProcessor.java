package com.ums.schedule.application.target.processor;

import com.ums.schedule.adapter.storage.AwsS3FileMetadataResponse;
import com.ums.schedule.adapter.storage.AwsS3Repository;
import com.ums.schedule.application.target.exception.TargetUploadReportNotConfiguredException;
import com.ums.schedule.application.target.exception.TargetUploadReportNotFoundException;
import com.ums.schedule.application.target.processor.model.FileTargetUploadRequestResult;
import com.ums.schedule.application.target.provider.FileTargetUploadResult;
import com.ums.schedule.application.target.reader.model.FileTargetUploadRequestedEvent;
import com.ums.schedule.application.target.TargetUploadRequestService;
import com.ums.schedule.application.target.exception.TargetUploadProcessException;
import com.ums.schedule.application.target.report.model.TargetUploadRequestResult;
import com.ums.schedule.common.code.api.CommonErrorCode;
import com.ums.schedule.common.code.api.TargetUploadErrorCode;
import com.ums.schedule.common.code.target_upload.TargetUploadConfiguration;
import com.ums.schedule.common.exception.file.FileNotFoundException;
import com.ums.schedule.common.util.UuidUtil;
import com.ums.schedule.config.properties.TargetUploadProperties;
import com.ums.schedule.domain.target.upload.TargetUploadReport;
import com.ums.schedule.domain.target.upload.TargetUploadReportJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class FileTargetUploadProcessor {
    private final TargetUploadProperties properties;
    private final AwsS3Repository fileRepository;
    private final TargetUploadReportJpaRepository jpaRepository;
    private final TargetUploadRequestService requestService;
    private final ApplicationEventPublisher publisher;

    @Transactional
    public FileTargetUploadRequestResult request(String id) {
        UUID uploadId = UUID.fromString(id);
        TargetUploadReport report = jpaRepository.findById(uploadId)
                .orElseThrow(() -> TargetUploadReportNotFoundException.of(uploadId));

        try {
            Integer partitionSize = properties.getPartitionSize();
            Integer batchSize = properties.getBatchSize();
            Long fileLimitSize = properties.getFileLimitSize();
            AwsS3FileMetadataResponse response = getFileMetadataAndUploadRequest(report, fileLimitSize);
            TargetUploadRequestResult result = requestService.request(report);
            publishTargetUploadRequestedEvent(report, result, partitionSize, batchSize);
            return FileTargetUploadRequestResult.of(report, response);
        } catch (FileNotFoundException e) {
            throw TargetUploadProcessException.of(
                    TargetUploadErrorCode.UPLOAD_FILE_NOT_EXIST, id, report.getUploadKey()
            );
        }
    }

    private void publishTargetUploadRequestedEvent(TargetUploadReport report, TargetUploadRequestResult result, Integer partitionSize, Integer batchSize) {
        FileTargetUploadRequestedEvent event = FileTargetUploadRequestedEvent.of(report, result, getPartitionSize(partitionSize), getBatchSize(batchSize));
        publisher.publishEvent(event);
    }

    private Integer getBatchSize(Integer batchSize) {
        if(batchSize == 0) {
            throw TargetUploadReportNotConfiguredException.of(TargetUploadConfiguration.FILE_BATCH_SIZE);
        }
        return batchSize;
    }

    private Integer getPartitionSize(Integer partitionSize) {
        if(partitionSize == 0) {
            throw TargetUploadReportNotConfiguredException.of(TargetUploadConfiguration.PARTITION_SIZE);
        }
        return partitionSize;
    }

    private AwsS3FileMetadataResponse getFileMetadataAndUploadRequest(TargetUploadReport report, Long fileLimitSize) {
        AwsS3FileMetadataResponse response = fileRepository.getFileMetadata(report.getUploadKey());
        Long fileSize = getFileSize(fileLimitSize, response.contentLength());
        report.assignFileSize(fileSize);
        return response;
    }

    private Long getFileSize(Long fileLimitSize, Long contentLength) {
        if(fileLimitSize < contentLength) {
            if(isFileLimitSizeEmpty(fileLimitSize)) {
                throw TargetUploadReportNotConfiguredException.of(TargetUploadConfiguration.FILE_LIMIT_SIZE);
            }
            throw TargetUploadProcessException.of(TargetUploadErrorCode.TARGET_UPLOAD_LIMIT_EXCEEDED);
        }
        return contentLength;
    }

    private boolean isFileLimitSizeEmpty(Long fileLimitSize) {
        return fileLimitSize == null || fileLimitSize == 0L;
    }
}
