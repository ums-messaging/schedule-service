package com.ums.schedule.application.target.reader;

import com.alibaba.excel.EasyExcel;
import com.ums.schedule.adapter.storage.AwsS3Repository;
import com.ums.schedule.application.target.exception.TargetUploadReportNotFoundException;
import com.ums.schedule.application.target.uploader.TargetUploader;
import com.ums.schedule.application.target.report.TargetUploadReportService;
import com.ums.schedule.application.target.reader.model.FileTargetUploadRequestedEvent;
import com.ums.schedule.application.ums.common.target.context.SendTargetGroupedList;
import com.ums.schedule.common.code.mapper.EnumMapperValue;
import com.ums.schedule.domain.request.SendRequest;
import com.ums.schedule.domain.request.customer.CustomerRequestKey;
import com.ums.schedule.domain.target.upload.TargetUploadReport;
import com.ums.schedule.domain.target.upload.TargetUploadReportJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.io.InputStream;
import java.util.List;
import java.util.function.Consumer;


@Slf4j
@Component
@RequiredArgsConstructor
public class TargetUploadFileReader {
    private final List<TargetUploader> uploaders;
    private final TargetUploadReportService reportService;
    private final TargetUploadReportJpaRepository repository;
    private final AwsS3Repository fileRepository;

    @Async("targetUploadFileExecutor")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void listen(FileTargetUploadRequestedEvent event) {
        TargetUploadReport targetUpload = repository.findById(event.uploadId())
                .orElseThrow(() -> TargetUploadReportNotFoundException.of(event.uploadId()));
        SendRequest sendRequest = targetUpload.getSendRequest();

        EnumMapperValue channelTypeValue = EnumMapperValue.fromEnumMapperType(event.channelType());

        try {
            TargetUploader uploader = uploaders.stream()
                    .filter(v -> v.supports(channelTypeValue))
                    .findFirst()
                    .orElseThrow();

            Consumer<List<SendTargetGroupedList>> consumer =
                    uploader.upload(event.uploadId(), event.messageId());
            TargetUploadFileReaderListener listener =
                    new TargetUploadFileReaderListener(consumer, event.customerId(), event.partitionSize(), event.batchSize());

            long startMs = System.currentTimeMillis();
            InputStream inputStream = fileRepository.getFileContent(event.uploadKey());
            long endMs = System.currentTimeMillis();

            log.info("파일 읽는 시간 : {}", endMs - startMs);

            startMs = System.currentTimeMillis();

            EasyExcel.read(inputStream, listener)
                    .sheet()
                    .doRead();
            endMs = System.currentTimeMillis();
            log.info("엑셀 파일 처리 시간 : {}", endMs - startMs);

            reportService.reportingAndOnCompleted(targetUpload);
        } catch (Exception e) {
            e.printStackTrace();
            targetUpload.onFailed(e.getMessage());
        }
    }
}
