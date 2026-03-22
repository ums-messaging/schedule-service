package com.ums.schedule.attachment.infrastructure;

import com.ums.schedule.attachment.application.response.AwsS3FileMetadataResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.HeadObjectRequest;
import software.amazon.awssdk.services.s3.model.HeadObjectResponse;

@Component
@RequiredArgsConstructor
public class AwsS3Repository {
    private final S3Client s3Client;
    private final String BUCKET_NAME;

    public AwsS3FileMetadataResponse getFileMetadata(String key) {
        HeadObjectResponse response = s3Client.headObject(
                HeadObjectRequest.builder()
                        .bucket(BUCKET_NAME)
                        .key(key)
                        .build()
        );
        return new AwsS3FileMetadataResponse(
                response.contentType(),
                response.contentLength(),
                response.lastModified(),
                response.metadata()
        );
    }

    public String getFileContent(String key) {
        return "";
    }
}
