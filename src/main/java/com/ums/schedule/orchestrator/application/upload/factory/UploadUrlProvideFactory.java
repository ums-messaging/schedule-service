package com.ums.schedule.orchestrator.application.upload.factory;

import com.ums.schedule.common.code.mapper.EnumMapperValue;
import com.ums.schedule.orchestrator.application.upload.provider.SendRequestUploadUrlProvider;
import com.ums.schedule.orchestrator.application.upload.result.UploadUrlProvideResult;
import com.ums.schedule.orchestrator.domain.upload.SendRequestUpload;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class UploadUrlProvideFactory {
    private final List<SendRequestUploadUrlProvider> providers;

    public SendRequestUploadUrlProvider create(EnumMapperValue uploadType, SendRequestUpload upload) {
        return providers.stream()
                .filter(v -> v.supports(uploadType))
                .findFirst()
                .orElseThrow();
    }
}
