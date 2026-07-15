package com.ums.schedule.application.sendrequest.target.report;

import com.ums.schedule.adapter.storage.AwsS3Repository;
import com.ums.schedule.config.properties.TargetUploadProperties;
import com.ums.schedule.domain.request.target.upload.TargetUploadReport;
import com.ums.schedule.domain.request.target.upload.TargetUploadReportJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TargetUploadReportRequestService {
    private final TargetUploadReportJpaRepository repository;
    private final ApplicationEventPublisher publisher;
    private final AwsS3Repository fileRepository;
    private final TargetUploadProperties properties;

    @Transactional
    public TargetUploadReport request(UUID uploadId) {
//        TargetUploadReport targetUpload = repository.findById(uploadId).orElseThrow(() -> TargetUploadNotFoundException.of(uploadId));
//        AwsS3FileMetadataResponse response = fileRepository.getFileMetadata(targetUpload.getUploadKey());

//        TargetFileUploadRequestCommand command = TargetFileUploadRequestCommand.of(targetUpload, response, properties.getUploadMaxSize());
//        targetUpload.requestFileUpload(command);
//
//        TargetUploadRequestedEvent event = TargetUploadRequestedEvent.of(targetUpload.getUploadId(), command);
//        publisher.publishEvent(event);

//        return targetUpload;
        return null;
    }
}
