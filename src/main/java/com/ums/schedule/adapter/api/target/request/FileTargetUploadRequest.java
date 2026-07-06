package com.ums.schedule.adapter.api.target.request;

public record FileTargetUploadRequest(
        String uploadType,
        String uploadFormat
) {

}
