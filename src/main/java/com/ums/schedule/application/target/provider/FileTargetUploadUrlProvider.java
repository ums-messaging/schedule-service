package com.ums.schedule.application.target.provider;

import com.ums.schedule.adapter.storage.AwsS3Repository;
import com.ums.schedule.adapter.storage.PresigendUrlResponse;
import com.ums.schedule.application.target.exception.TargetUploadProcessException;
import com.ums.schedule.common.code.api.TargetUploadErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.awscore.exception.AwsServiceException;


@Service
@RequiredArgsConstructor
public class FileTargetUploadUrlProvider implements TargetUploadProvider {
    private final AwsS3Repository fileRepository;

    public FileTargetUploadResult provide(String objectKey) {
        try {
            PresigendUrlResponse response = fileRepository.generateUploadUrl(objectKey);
            return FileTargetUploadResult.of(response);
        } catch (AwsServiceException e) {
            throw TargetUploadProcessException.of(TargetUploadErrorCode.UPLOAD_KEY_GENERATION_FAILED, e);
        }
    }
}
