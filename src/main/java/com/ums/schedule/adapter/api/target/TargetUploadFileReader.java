package com.ums.schedule.adapter.api.target;

import com.alibaba.excel.EasyExcel;
import com.ums.schedule.adapter.storage.AwsS3Repository;
import com.ums.schedule.application.sendrequest.target.SendTargetReaderListener;
import com.ums.schedule.domain.request.target.upload.TargetUploadReport;
import com.ums.schedule.domain.request.target.upload.TargetUploadReportJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class TargetUploadFileReader {
    private final TargetUploadReportJpaRepository repository;
    private final AwsS3Repository fileRepository;
    private final ApplicationEventPublisher publisher;

    public void listen(UUID uploadId) {
        TargetUploadReport targetUpload = repository.findById(uploadId).orElseThrow();

        SendTargetReaderListener listener = new SendTargetReaderListener(targetUpload, publisher);

        InputStream inputStream = fileRepository.getFileContent(targetUpload.getUploadKey());
        EasyExcel.read(inputStream, listener)
                .sheet()
                .doRead();
    }
}
