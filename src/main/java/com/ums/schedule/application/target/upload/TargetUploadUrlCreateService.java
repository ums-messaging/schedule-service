package com.ums.schedule.application.target.upload;

import com.ums.schedule.adapter.storage.AwsS3Repository;
import com.ums.schedule.adapter.storage.PresigendUrlResponse;
import com.ums.schedule.code.EnumMapperValue;
import com.ums.schedule.code.send.ContentTypeEnum;
import com.ums.schedule.code.send.TargetUploadTypeEnum;
import com.ums.schedule.domain.target.event.TargetUploadEvent;
import com.ums.schedule.domain.request.SendRequestRepository;
import com.ums.schedule.domain.target.upload.TargetUpload;
import com.ums.schedule.domain.request.SendRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.ums.schedule.code.send.TargetUploadTypeEnum.FILE;


@Service
@RequiredArgsConstructor
public class TargetUploadUrlCreateService implements TargetUploadCreator {
    private final AwsS3Repository repository;
    private final SendRequestRepository requestRepository;

    public TargetUpload create(TargetUploadCreateCommand command) {
        SendRequest sendRequest = requestRepository.getReferenceById(command.requestId());
        PresigendUrlResponse response = repository.generateUploadUrl(ContentTypeEnum.CSV, command.customerId());
        TargetUpload targetUpload = TargetUpload.of(FILE, sendRequest);

        TargetUploadEvent targetUploadEvent = targetUpload.createTargetUploadUrlEvent(response.objectKey());

        return targetUpload;
    }

    @Override
    public boolean supports(EnumMapperValue mapperValue) {
        return FILE == TargetUploadTypeEnum.valueOf(mapperValue.code());
    }
}
