package com.ums.schedule.orchestrator.application.upload;

import com.ums.schedule.common.code.common.CommonCode;
import com.ums.schedule.common.code.mapper.EnumMapperFactory;
import com.ums.schedule.common.code.mapper.EnumMapperValue;
import com.ums.schedule.orchestrator.adaptor.in.rest.upload.request.SendRequestUploadRequest;
import com.ums.schedule.orchestrator.adaptor.in.config.SendRequestUploadConfigProvider;
import com.ums.schedule.orchestrator.adaptor.out.storage.AwsS3FileMetadataResponse;
import com.ums.schedule.orchestrator.adaptor.out.storage.AwsS3Repository;
import com.ums.schedule.orchestrator.application.upload.result.FileUploadRequestResult;
import com.ums.schedule.orchestrator.application.upload.result.SendRequestUploadRequestResult;
import com.ums.schedule.orchestrator.domain.upload.FileUploadMetadata;
import com.ums.schedule.orchestrator.domain.common.ProvideUrl;
import com.ums.schedule.orchestrator.domain.upload.SendRequestUpload;
import com.ums.schedule.orchestrator.domain.upload.SendRequestUploadRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Slf4j
@RequiredArgsConstructor
public class SendRequestUploadRequestService {
    private final EnumMapperFactory factory;
    private final SendRequestUploadConfigProvider provider;
    private final SendRequestUploadRepository repository;
    private final AwsS3Repository fileRepository;

    @Transactional
    public SendRequestUploadRequestResult request(String customerId, String objectkey) {
        SendRequestUpload sendRequestUpload = repository.findByCustomerIdAndObjectKey(customerId, objectkey);
        FileUploadMetadata metadata = getFileUploadMetadata(sendRequestUpload);

        sendRequestUpload.requestFileUpload(sendRequestUpload.objectKey(), metadata);
        SendRequestUpload saved = repository.save(sendRequestUpload);

        return SendRequestUploadRequestResult.of(saved);
    }

    private FileUploadMetadata getFileUploadMetadata(SendRequestUpload sendRequestUpload) {
        AwsS3FileMetadataResponse response = fileRepository.getFileMetadata(sendRequestUpload.objectKey());
        EnumMapperValue contentType = factory.findEnumMapperValue(CommonCode.FILE_CONTENT_TYPE, response.contentType());
        FileUploadMetadata metadata = FileUploadMetadata.of(contentType, response.contentLength(), provider.getFileLimitSize());
        return metadata;
    }

    @Transactional
    public void request(String customerId, String objectKey, List<SendRequestUploadRequest> requests) {

    }
}

