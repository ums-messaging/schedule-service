package com.ums.schedule.orchestrator.application.upload;

import com.ums.schedule.common.code.mapper.EnumMapperFactory;
import com.ums.schedule.common.code.mapper.EnumMapperValue;
import com.ums.schedule.common.code.target_upload.TargetUploadReportCode;
import com.ums.schedule.orchestrator.adaptor.in.rest.upload.request.SendRequestUploadCreateRequest;
import com.ums.schedule.orchestrator.application.upload.command.SendRequestUploadCreateCommand;
import com.ums.schedule.orchestrator.application.upload.factory.UploadUrlProvideFactory;
import com.ums.schedule.orchestrator.application.upload.provider.SendRequestUploadUrlProvider;
import com.ums.schedule.orchestrator.application.upload.result.SendRequestUploadResult;
import com.ums.schedule.orchestrator.domain.common.ProvideUrl;
import com.ums.schedule.orchestrator.domain.upload.SendRequestUpload;
import com.ums.schedule.orchestrator.domain.upload.SendRequestUploadRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SendRequestUploadService {
    private final EnumMapperFactory enumFactory;
    private final UploadUrlProvideFactory urlFactory;
    private final SendRequestUploadRepository repository;

    @Transactional
    public SendRequestUploadResult create(String customerId, SendRequestUploadCreateRequest request) {
        EnumMapperValue uploadType = enumFactory.findEnumMapperValue(TargetUploadReportCode.UPLOAD_TYPE, request.type());
        SendRequestUploadCreateCommand command = SendRequestUploadCreateCommand.of(uploadType, customerId, request);
        SendRequestUpload upload = SendRequestUpload.of(command);
        SendRequestUploadUrlProvider provider = urlFactory.create(uploadType, upload);

        ProvideUrl provideUrl = provider.provide(upload.getCustomerId(), request.uploadKey());
        upload.provideUrl(provideUrl);

        repository.save(upload);

        return SendRequestUploadResult.of(upload);
    }
}
