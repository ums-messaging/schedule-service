package com.ums.schedule.application.sendrequest;

import com.ums.schedule.adapter.storage.AwsS3Repository;
import com.ums.schedule.adapter.storage.PresigendUrlResponse;
import com.ums.schedule.application.sendrequest.target.result.FileTargetUploadResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class FileTargetUploadUrlProvider implements TargetUploadHandler {
    private final AwsS3Repository fileRepository;

    public FileTargetUploadResult provide(String objectKey) {
        PresigendUrlResponse response = fileRepository.generateUploadUrl(objectKey);
        return FileTargetUploadResult.of(response);
    }
}
