package com.ums.schedule.adapter.storage;

import java.time.Instant;
import java.util.Map;

public record AwsS3FileMetadataResponse(
        String contentType,
        Long contentLength,
        Instant lastModified,
        Map<String, String> metadata
) {


}
