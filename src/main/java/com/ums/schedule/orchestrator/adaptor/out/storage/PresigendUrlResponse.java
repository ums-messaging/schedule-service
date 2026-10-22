package com.ums.schedule.orchestrator.adaptor.out.storage;

import java.time.Duration;
import java.time.Instant;

public record PresigendUrlResponse(
        String objectKey,
        String presignedUrl,
        Instant expiredAt
) {
    public static PresigendUrlResponse of(String key, String url, Duration expiredDuration) {
        Instant expiredAt = Instant.now().plus(expiredDuration);
        return new PresigendUrlResponse(key, url, expiredAt);
    }
}
