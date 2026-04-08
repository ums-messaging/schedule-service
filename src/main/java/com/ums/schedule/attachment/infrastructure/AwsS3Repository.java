package com.ums.schedule.attachment.infrastructure;

import com.ums.schedule.attachment.application.response.AwsS3FileMetadataResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.HeadObjectRequest;
import software.amazon.awssdk.services.s3.model.HeadObjectResponse;

import java.io.InputStreamReader;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;

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

    public InputStreamReader getFileContent(String key) {
        ResponseInputStream<GetObjectResponse> inputStream = s3Client.getObject(
                GetObjectRequest.builder()
                        .build()
        );
        return new InputStreamReader(inputStream, StandardCharsets.UTF_8);
    }
}
