package com.ums.schedule.adapter.storage;

import com.ums.schedule.common.code.api.FileErrorCode;
import com.ums.schedule.common.exception.file.AmazonS3FileException;
import com.ums.schedule.common.exception.file.FileNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.awscore.exception.AwsServiceException;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.Duration;

@Component
@RequiredArgsConstructor
public class AwsS3Repository {
    private final S3Client s3Client;
    private final String BUCKET_NAME = "ums-file-bucket-01";

    public AwsS3FileMetadataResponse getFileMetadata(String key) {
        if(!existsFile(key)) {
            throw FileNotFoundException.of(key);
        }
        HeadObjectResponse response;

        try {
            response = s3Client.headObject(
                    HeadObjectRequest.builder()
                            .bucket(BUCKET_NAME)
                            .key(key)
                            .build()
            );
            return new AwsS3FileMetadataResponse(
                    key,
                    response.contentType(),
                    response.contentLength(),
                    response.lastModified(),
                    response.metadata()
            );
        } catch (NoSuchKeyException e) {
            throw AmazonS3FileException.of(FileErrorCode.FILE_METADATA_LOAD_FAILED, key, e);
        }
    }

    public boolean existsFile(String key) {
        try {
            s3Client.headObject(
                    HeadObjectRequest.builder()
                            .bucket(BUCKET_NAME)
                            .key(key)
                            .build()
            );
            return true;
        } catch (NoSuchKeyException e) {
            return false;
        }
    }

    public String getFileStringContent(String key) {
        InputStream is = getFileContent(key);
        InputStreamReader reader = new InputStreamReader(is, StandardCharsets.UTF_8);
        return reader.toString();
    }

    public InputStream getFileContent(String key) {
        ResponseInputStream<GetObjectResponse> inputStream = s3Client.getObject(
                GetObjectRequest.builder()
                        .key(key)
                        .build()
        );
        return inputStream;
    }

    public PresigendUrlResponse generateUploadUrl(String objectKey) {
        try {
            S3Presigner presigner = S3Presigner.create();
            PutObjectRequest request = PutObjectRequest.builder()
                    .bucket(BUCKET_NAME)
                    .key(objectKey)
                    .build();

            Duration expiredDuration = Duration.ofHours(3);
            PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                    .signatureDuration(expiredDuration)
                    .putObjectRequest(request)
                    .build();

            String url = presigner.presignPutObject(presignRequest)
                    .url().toString();

            presigner.close();
            return PresigendUrlResponse.of(objectKey, url, expiredDuration);
        } catch (AwsServiceException e) {
            throw AmazonS3FileException.of(FileErrorCode.UPLOAD_URL_GENERATED_FAIL, objectKey, e);
        }
    }

    public PresigendUrlResponse generateDownloadUrl(String objectKey) {
        S3Presigner presigner = S3Presigner.create();
        GetObjectRequest objectRequest = GetObjectRequest.builder()
                .key(objectKey)
                .bucket(BUCKET_NAME)
                .build();
        Duration expiredDuration = Duration.ofHours(3);

        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(expiredDuration)
                .getObjectRequest(objectRequest)
                .build();

        String url = presigner.presignGetObject(presignRequest).url().toString();

        return PresigendUrlResponse.of(objectKey, url, expiredDuration);
    }

    public String upload(File file, String key) {
        PutObjectRequest request = null;
        try {
            request = PutObjectRequest.builder()
                    .bucket(BUCKET_NAME)
                    .key(key)
                    .contentType(Files.probeContentType(file.toPath()))
                    .build();
            s3Client.putObject(
                    request,
                    RequestBody.fromFile(file)
            );

            return key;
        } catch (IOException e) {
            throw AmazonS3FileException.of(FileErrorCode.FILE_UPLOAD_FAIL, key, e);

        }
    }
}
