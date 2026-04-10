package com.ums.schedule.send.application.service;

import com.ums.schedule.attachment.application.response.AwsS3FileMetadataResponse;
import com.ums.schedule.attachment.infrastructure.AwsS3Repository;
import com.ums.schedule.repository.SendRequestRepository;
import com.ums.schedule.send.application.reader.SendTargetReader;
import com.ums.schedule.send.domain.request.EmailSendRequest;
import com.ums.schedule.send.domain.request.upload.TargetUpload;
import com.ums.schedule.send.domain.request.upload.repository.TargetUploadRepository;
import com.ums.schedule.send.domain.request.upload.status.TargetUploadStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.InputStream;

@Service
@RequiredArgsConstructor
public class TargetUploadReqeustService {
    private final AwsS3Repository fileRepository;
    private final TargetUploadRepository repository;
    private final SendTargetReader targetReader;

    public TargetUpload upload(Long uploadId) {
        TargetUpload targetUpload = repository.findById(uploadId).orElseThrow();

        String objectKey = targetUpload.getObjectKey();
        validate(objectKey);

        InputStream fileContent = fileRepository.getFileContent(objectKey);
        TargetUploadStatus status = targetUpload.upload();
        return targetReader.read(targetUpload, fileContent);
    }

    private void validate(String objectKey) {
        AwsS3FileMetadataResponse fileMetadata = fileRepository.getFileMetadata(objectKey);
    }
}
