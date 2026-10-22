package com.ums.schedule.orchestrator.application.upload.provider;

import com.ums.schedule.common.code.mapper.EnumMapperSelector;
import com.ums.schedule.orchestrator.domain.common.ProvideUrl;

public interface SendRequestUploadUrlProvider extends EnumMapperSelector {
    ProvideUrl provide(String customerId, String objectKey);
}
