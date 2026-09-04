package com.ums.schedule.fixture.file;

import com.ums.schedule.adapter.storage.AwsS3FileMetadataResponse;

import java.time.Instant;
import java.util.Map;

public class AwsS3FileMetadataResponseBuilder {
    private String key;
    private String contentType;
    private Long contentLength;
    private Instant lastModified;
    private Map<String, String> metadata;

    public static AwsS3FileMetadataResponseBuilder builder() {
        return new AwsS3FileMetadataResponseBuilder();
    }

    private AwsS3FileMetadataResponseBuilder() {
        this.key = "/target/upload/hyejin_company/target.xlsx";
        this.contentLength = 500L;
        this.lastModified = Instant.now().minusSeconds(36000);
    }

    public AwsS3FileMetadataResponseBuilder contentLength(Long contentLength) {
        this.contentLength = contentLength;
        return this;
    }

    public AwsS3FileMetadataResponse build() {
        return new AwsS3FileMetadataResponse(
                key,
                contentType,
                contentLength,
                lastModified,
                metadata
        );
    }
}
