package com.ums.schedule.adapter.api.target.request;

import java.util.List;

public record JsonTargetUploadRequest(
        List<SendTargetCreateRequest> targetList
) {
}
