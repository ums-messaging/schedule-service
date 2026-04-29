package com.ums.schedule.application.target;

import com.ums.schedule.adapter.storage.AwsS3FileMetadataResponse;
import com.ums.schedule.adapter.storage.AwsS3Repository;
import com.ums.schedule.domain.target.event.TargetUploadRequestedEvent;
import com.ums.schedule.domain.target.upload.TargetUpload;
import com.ums.schedule.domain.target.upload.TargetUploadRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TargetUploadRequestService {
    private final AwsS3Repository fileRepository;
    private final TargetUploadRepository repository;
    private final ApplicationEventPublisher publisher;

    @Transactional
    public TargetUpload upload(TargetUploadRequestedEvent event) {
        TargetUpload targetUpload = repository.findById(event.uploadId()).orElseThrow();

        validate(targetUpload.getObjectKey());
        publisher.publishEvent(event);

        return targetUpload;
    }

    private void validate(String objectKey) {
        AwsS3FileMetadataResponse fileMetadata = fileRepository.getFileMetadata(objectKey);
    }
}
