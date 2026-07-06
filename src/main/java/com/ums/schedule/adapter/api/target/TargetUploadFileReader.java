package com.ums.schedule.adapter.api.target;

import com.ums.schedule.adapter.storage.AwsS3Repository;
import com.ums.schedule.application.sendrequest.target.assembler.SendTargetAssembler;
import com.ums.schedule.domain.sendrequest.target.upload.TargetUploadReportJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class TargetUploadFileReader {
    private final AwsS3Repository storageRepository;
    private final TargetUploadReportJpaRepository repository;
    private final ApplicationEventPublisher publisher;
    private final Map<String, SendTargetAssembler> factoryMap;

//    public void listen(TargetUploadRequestedEvent event) {
//        TargetUpload targetUpload = repository.findById(event.uploadId()).orElseThrow();
//        ChannelFactory factory = factoryMap.get(event.channelType());
//        ChannelTemplate template = factory.getTemplate(event.requestId(), event.templateKey());
//        TargetUploadCreatedEvent createdEvent = new TargetUploadCreatedEvent(event.uploadId(), event.channelType(), template);
//        InputStream inputStream = storageRepository.getFileContent(event.objectKey());
//
//        SendTargetReaderListener listener =
//                new SendTargetReaderListener(factoryMap.get(event.channelType()), createdEvent, targetUploadService);
//
//        EasyExcel.read(inputStream, listener)
//                .sheet()
//                .doRead();
//
//        publisher.publishEvent(targetUpload.uploadComplete(listener.getTotalCount()));
//    }
}
