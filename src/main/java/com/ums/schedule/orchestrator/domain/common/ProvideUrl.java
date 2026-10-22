package com.ums.schedule.orchestrator.domain.common;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public record ProvideUrl (
        String objectKey,
        String provideUrl,
        LocalDateTime expiredAt
) {
    public static ProvideUrl of(String objectKey, String provideUrl, Instant expiredAt) {
        LocalDateTime toExpiredAt = LocalDateTime.ofInstant(expiredAt, ZoneId.of("Asia/Seoul"));
        return new ProvideUrl(
                objectKey,
                provideUrl,
                toExpiredAt
        );
    }
    public static ProvideUrl of(String objectKey, String provideUrl, LocalDateTime expiredAt) {
        return new ProvideUrl(
                objectKey,
                provideUrl,
                expiredAt
        );
    }
    public static ProvideUrl of(String objectKey) {
        return new ProvideUrl(objectKey, null, null);
    }
}
