package com.ums.schedule.application.target.upload;

import com.ums.schedule.application.ChannelFactory;
import com.ums.schedule.application.target.dto.SendTargetDto;
import com.ums.schedule.code.EnumMapperValue;
import com.ums.schedule.code.send.TargetUploadTypeEnum;
import com.ums.schedule.domain.channel.ChannelTemplate;
import com.ums.schedule.domain.target.event.TargetUploadRequestedEvent;
import com.ums.schedule.domain.request.SendRequest;
import com.ums.schedule.domain.request.SendRequestRepository;
import com.ums.schedule.domain.target.SendTarget;
import com.ums.schedule.domain.target.upload.TargetUpload;
import com.ums.schedule.domain.target.upload.TargetUploadRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

import static com.ums.schedule.code.send.TargetUploadTypeEnum.JSON;

@Component
@RequiredArgsConstructor
public class TargetUploadRequestService {
    private final TargetUploadRepository targetUploadRepository;
    private final SendRequestRepository requestRepository;
    private final Map<String, ChannelFactory> factoryMap;

    @Transactional
    public TargetUpload create(TargetUploadRequestedEvent event, List<SendTargetDto> targetDtoList) {
        SendRequest sendRequest = requestRepository.getReferenceById(event.requestId());
        ChannelFactory factory = factoryMap.get(event.channelType().value());
        ChannelTemplate template = factory.getTemplate(event.requestId(), event.templateKey());
        TargetUpload targetUpload = TargetUpload.of(JSON, sendRequest);
        targetUploadRepository.saveAndFlush(targetUpload);
        factory.makeMessage(targetUpload.getUploadId(), template, targetDtoList);
        return targetUpload;
    }

    public boolean supports(EnumMapperValue mapperValue ) {
        return TargetUploadTypeEnum.valueOf(mapperValue.code()) == JSON;
    }
}
