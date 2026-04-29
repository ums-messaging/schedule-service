package com.ums.schedule.application.target.upload;

import com.ums.schedule.application.request.SendRequestService;
import com.ums.schedule.code.EnumMapperFactory;
import com.ums.schedule.code.EnumMapperValue;
import com.ums.schedule.code.send.ChannelTypeEnum;
import com.ums.schedule.code.send.SendRequestEnumMapper;
import com.ums.schedule.domain.request.SendRequestEvent;
import com.ums.schedule.domain.target.event.TargetUploadEvent;
import com.ums.schedule.domain.request.SendRequest;
import com.ums.schedule.adapter.api.send.SendCreateRequest;
import com.ums.schedule.domain.target.upload.TargetUploadRepository;
import com.ums.schedule.domain.target.upload.TargetUpload;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Component
@RequiredArgsConstructor
public class TargetUploadService {
    private final EnumMapperFactory factory;
    private final SendRequestService sendRequestCreator;
    private final List<TargetUploadCreator> targetUploadCreators;
    private final ApplicationEventPublisher publisher;
    private final TargetUploadRepository repository;

    @Transactional
    public TargetUpload create(String customerId, ChannelTypeEnum channelType, SendCreateRequest command) {
        EnumMapperValue uploadType = factory.findEnumMapperValue(SendRequestEnumMapper.TARGET_UPLOAD_TYPE, command.uploadType());

        TargetUploadCreator uploadCreator = targetUploadCreators.stream()
                .filter(upload -> upload.supports(uploadType))
                .findFirst()
                .orElseThrow();

        SendRequest request = sendRequestCreator.createSendRequest(customerId, channelType, command);

        return uploadCreator.create(TargetUploadCreateCommand.of(customerId, request, command.targetList()));
    }

    @Transactional
    public TargetUpload upload(Long uploadId) {
        TargetUpload targetUpload = repository.findById(uploadId).orElseThrow();
        SendRequestEvent requestEvent = targetUpload.requestTargetUpload();
        publisher.publishEvent(requestEvent.getEvent());
        return targetUpload;
    }
}
