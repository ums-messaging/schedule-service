package com.ums.schedule.send.application.reader;

import com.alibaba.excel.EasyExcel;
import com.ums.schedule.attachment.infrastructure.AwsS3Repository;
import com.ums.schedule.message.domain.SendMessage;
import com.ums.schedule.send.application.assembler.TargetDbUploadService;
import com.ums.schedule.send.application.service.SendTargetService;
import com.ums.schedule.send.code.TargetUploadTypeEnum;
import com.ums.schedule.send.domain.request.EmailSendRequest;
import com.ums.schedule.send.domain.request.SendRequest;
import com.ums.schedule.send.domain.request.upload.TargetUpload;
import com.ums.schedule.send.domain.request.upload.status.TargetCompletedStatus;
import com.ums.schedule.send.domain.request.upload.status.TargetUploadedStatus;
import com.ums.schedule.template.infrastructure.TemplateClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;

import java.io.InputStream;

import static com.ums.schedule.common.code.EnumMapperValue.fromEnumMapperType;

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
