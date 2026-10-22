package com.ums.schedule.orchestrator.adaptor.in.rest.upload.request;

import java.util.Map;

public record SendRequestUploadRequest(
        String targetKey,
        String targetName,
        String contact,
        String birthday,
        Map<String, Object> messageVariable
) {
}
