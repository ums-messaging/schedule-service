package com.ums.schedule.application.target.reader;

import com.alibaba.excel.EasyExcel;
import com.ums.schedule.application.target.upload.TargetDbUploadService;
import com.ums.schedule.domain.target.upload.TargetUpload;
import com.ums.schedule.domain.target.upload.status.TargetCompletedStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.InputStream;

@Component
@RequiredArgsConstructor
public class SendTargetReader {
    private final TargetDbUploadService uploadService;

    public TargetUpload read(TargetUpload targetUpload, InputStream inputStream) {
        SendTargetReaderListener listener =
                new SendTargetReaderListener(targetUpload, uploadService);

        EasyExcel.read(inputStream, listener)
                .sheet()
                .doRead();

        targetUpload.changeStatus(new TargetCompletedStatus());
        return targetUpload;
    }
}
