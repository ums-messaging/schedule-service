package com.ums.schedule.application.event.listener;

import com.alibaba.excel.EasyExcel;
import com.ums.schedule.adapter.storage.AwsS3Repository;
import com.ums.schedule.application.ChannelFactory;
import com.ums.schedule.application.target.reader.SendTargetReaderListener;
import com.ums.schedule.application.target.upload.TargetDbUploadService;
import com.ums.schedule.domain.channel.ChannelTemplate;
import com.ums.schedule.domain.target.event.TargetUploadCreatedEvent;
import com.ums.schedule.domain.target.event.TargetUploadRequestedEvent;
import com.ums.schedule.domain.target.upload.TargetUploadRepository;
import com.ums.schedule.domain.target.upload.TargetUpload;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.io.InputStream;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class TargetUploadRequestedEventListener {
    private final AwsS3Repository storageRepository;
    private final TargetUploadRepository repository;
    private final TargetDbUploadService targetUploadService;
    private final ApplicationEventPublisher publisher;
    private final Map<String, ChannelFactory> factoryMap;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void listen(TargetUploadRequestedEvent event) {
        TargetUpload targetUpload = repository.findById(event.uploadId()).orElseThrow();
        ChannelFactory factory = factoryMap.get(event.channelType());
        ChannelTemplate template = factory.getTemplate(event.requestId(), event.templateKey());
        TargetUploadCreatedEvent createdEvent = new TargetUploadCreatedEvent(event.uploadId(), event.channelType(), template);
        InputStream inputStream = storageRepository.getFileContent(event.objectKey());

        SendTargetReaderListener listener =
                new SendTargetReaderListener(factoryMap.get(event.channelType()), createdEvent, targetUploadService);

        EasyExcel.read(inputStream, listener)
                .sheet()
                .doRead();

        publisher.publishEvent(targetUpload.uploadComplete(listener.getTotalCount()));
    }
}
