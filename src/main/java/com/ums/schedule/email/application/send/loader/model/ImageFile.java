package com.ums.schedule.email.application.send.loader.model;

import com.github.f4b6a3.uuid.UuidCreator;

import java.util.UUID;

public record ImageFile(
        UUID contentId,
        String src
) {
    public static ImageFile of(String src) {
        UUID contentId = UuidCreator.getTimeOrdered();
        return new ImageFile(contentId, src);
    }
}
