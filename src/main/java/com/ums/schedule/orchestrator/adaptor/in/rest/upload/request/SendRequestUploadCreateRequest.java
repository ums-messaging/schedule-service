package com.ums.schedule.orchestrator.adaptor.in.rest.upload.request;

public record SendRequestUploadCreateRequest(
        String type,
        String uploadKey,
        String title,
        String description
) {
}
