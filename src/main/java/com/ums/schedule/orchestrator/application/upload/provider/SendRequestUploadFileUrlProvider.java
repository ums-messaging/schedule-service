package com.ums.schedule.orchestrator.application.upload.provider;

import com.ums.schedule.common.code.mapper.EnumMapperValue;
import com.ums.schedule.orchestrator.adaptor.in.config.SendRequestUploadConfigProvider;
import com.ums.schedule.orchestrator.adaptor.out.storage.AwsS3Repository;
import com.ums.schedule.orchestrator.adaptor.out.storage.PresigendUrlResponse;
import com.ums.schedule.orchestrator.application.upload.config.SendRequestUploadConfig;
import com.ums.schedule.orchestrator.application.upload.exception.TargetUploadProcessException;
import com.ums.schedule.common.code.api.TargetUploadErrorCode;
import com.ums.schedule.orchestrator.common.code.UploadType;
import com.ums.schedule.orchestrator.domain.common.ProvideUrl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.awscore.exception.AwsServiceException;


@Service
@RequiredArgsConstructor
public class SendRequestUploadFileUrlProvider implements SendRequestUploadUrlProvider {
    private final SendRequestUploadConfigProvider provider;
    private final AwsS3Repository fileRepository;

    @Override
    public boolean supports(EnumMapperValue uploadType) {
        return UploadType.FILE == UploadType.valueOf(uploadType.code());
    }

    public ProvideUrl provide(String customerId, String objectKey) {
        try {
            String key = generateObjectKey(customerId, objectKey);
            PresigendUrlResponse response = fileRepository.generateUploadUrl(key);
            return ProvideUrl.of(key, response.presignedUrl(), response.expiredAt());
        } catch (AwsServiceException e) {
            throw TargetUploadProcessException.of(TargetUploadErrorCode.UPLOAD_KEY_GENERATION_FAILED, e);
        }
    }

    private String generateObjectKey(String customerId, String uploadKey) {
        SendRequestUploadConfig config = provider.load();
        return config.uploadDir() + "/" + customerId + "/" + uploadKey;
    }
}
