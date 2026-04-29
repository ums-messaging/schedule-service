package com.ums.schedule.adapter.storage;

import com.ums.schedule.code.send.ContentTypeEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.io.*;
import java.nio.file.Files;
import java.time.Duration;

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

    public InputStream getFileContent(String key) {
        ResponseInputStream<GetObjectResponse> inputStream = s3Client.getObject(
                GetObjectRequest.builder()
                        .build()
        );
        return inputStream;
    }

    public PresigendUrlResponse generateUploadUrl(ContentTypeEnum contentTypeValue, String customerId) {
        S3Presigner presigner = S3Presigner.create();
        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(BUCKET_NAME)
                .key(customerId)
                .contentType(contentTypeValue.value())
                .build();

        Duration expiredDuration = Duration.ofHours(3);
        PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                .signatureDuration(expiredDuration)
                .putObjectRequest(request)
                .build();

        String url = presigner.presignPutObject(presignRequest)
                .url().toString();

        presigner.close();
        return PresigendUrlResponse.of(customerId, url, expiredDuration);
    }

    public String upload(File file, String key) throws IOException {
        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(BUCKET_NAME)
                .key(key)
                .contentType(Files.probeContentType(file.toPath()))
                .build();

        s3Client.putObject(
                request,
                RequestBody.fromFile(file)
        );
        return key;
    }
}
