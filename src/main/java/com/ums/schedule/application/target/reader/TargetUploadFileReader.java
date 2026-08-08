package com.ums.schedule.application.target.reader;

import com.alibaba.excel.EasyExcel;
import com.ums.schedule.adapter.storage.AwsS3Repository;
import com.ums.schedule.application.sendrequest.target.data.TargetMessageData;
import com.ums.schedule.application.target.exception.TargetUploadProcessException;
import com.ums.schedule.application.target.exception.TargetUploadReportNotFoundException;
import com.ums.schedule.application.target.reader.model.TargetRowResult;
import com.ums.schedule.application.target.uploader.TargetUploader;
import com.ums.schedule.application.target.report.TargetUploadReportService;
import com.ums.schedule.application.target.reader.model.FileTargetUploadRequestedEvent;
import com.ums.schedule.common.code.api.TargetUploadErrorCode;
import com.ums.schedule.common.code.mapper.EnumMapperValue;
import com.ums.schedule.domain.target.upload.TargetUploadReport;
import com.ums.schedule.domain.target.upload.TargetUploadReportJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.io.InputStream;
import java.util.List;
import java.util.function.Consumer;

@Component
@RequiredArgsConstructor
public class TargetUploadFileReader {
    private final List<TargetUploader> uploaders;
    private final TargetUploadReportService reportService;
    private final TargetUploadReportJpaRepository repository;
    private final AwsS3Repository fileRepository;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void listen(FileTargetUploadRequestedEvent event) {
        TargetUploadReport targetUpload = repository.findById(event.uploadId())
                .orElseThrow(() -> TargetUploadReportNotFoundException.of(event.uploadId()));

        EnumMapperValue channelTypeValue = EnumMapperValue.fromEnumMapperType(event.channelType());

        try {
            TargetUploader uploader = uploaders.stream()
                    .filter(v -> v.supports(channelTypeValue))
                    .findFirst()
                    .orElseThrow();

            Consumer<List<TargetRowResult>> consumer =
                    uploader.upload(targetUpload, event.messageId());

            TargetUploadFileReaderListener listener =
                    new TargetUploadFileReaderListener(consumer, event.batchSize());

            InputStream inputStream = fileRepository.getFileContent(event.uploadKey());

            EasyExcel.read(inputStream, listener)
                    .sheet()
                    .doRead();

            reportService.reportingAndOnCompleted(targetUpload);
        } catch (Exception e) {
            targetUpload.onFailed(e.getMessage());
        }
    }
}
