package com.ums.schedule.application.target;

import com.ums.schedule.adapter.storage.AwsS3FileMetadataResponse;
import com.ums.schedule.adapter.storage.AwsS3Repository;
import com.ums.schedule.domain.target.upload.TargetUpload;
import com.ums.schedule.domain.target.TargetUploadRepository;
import com.ums.schedule.domain.target.upload.status.TargetUploadStatus;
import com.ums.schedule.application.target.reader.SendTargetReader;
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
