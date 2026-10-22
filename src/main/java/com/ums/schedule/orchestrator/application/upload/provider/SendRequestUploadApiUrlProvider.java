package com.ums.schedule.orchestrator.application.upload.provider;

import com.ums.schedule.common.code.mapper.EnumMapperValue;
import com.ums.schedule.orchestrator.common.code.UploadType;
import com.ums.schedule.orchestrator.domain.common.ProvideUrl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class SendRequestUploadApiUrlProvider implements SendRequestUploadUrlProvider {
    @Override
    public boolean supports(EnumMapperValue uploadType) {
        return UploadType.JSON == UploadType.valueOf(uploadType.code());
    }

    @Override
    public ProvideUrl provide(String customerId, String objectKey) {
        return ProvideUrl.of(objectKey);
    }
}
