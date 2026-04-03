package com.ums.schedule.send.application.reader;

import com.alibaba.excel.EasyExcel;
import com.ums.schedule.message.domain.SendMessage;
import com.ums.schedule.send.application.service.SendTargetService;
import com.ums.schedule.send.code.TargetUploadTypeEnum;
import com.ums.schedule.send.domain.request.SendRequest;
import com.ums.schedule.send.domain.request.upload.TargetUpload;
import com.ums.schedule.send.domain.request.upload.status.TargetCompletedStatus;
import com.ums.schedule.send.domain.request.upload.status.TargetUploadedStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;

import static com.ums.schedule.common.code.EnumMapperValue.fromEnumMapperType;

@Component
@RequiredArgsConstructor
public class SendTargetReader {
//    private final TargetUploadRepository repository;
    private final S3Client s3Client;
    private final SendTargetService targetService;


    public void read(String uploadId) {
        TargetUpload targetUpload = TargetUpload.of(fromEnumMapperType(TargetUploadTypeEnum.FILE));
        SendRequest request = targetUpload.getSendRequest();
        SendMessage message = request.getSendMessage();
        // Dto
        // 1. message 제목, 템플릿에 있는 전문
        // 2. securityType이 있는지 확인

        targetUpload.changeStatus(new TargetUploadedStatus());
        SendTargetReaderListener listener =
                new SendTargetReaderListener(request, targetService);

        ResponseInputStream<GetObjectResponse> inputStream = s3Client.getObject(
                GetObjectRequest.builder()
                        .bucket("")
                        .key(targetUpload.getObjectKey())
                        .build()
        );
        targetUpload.changeStatus(new TargetUploadedStatus());

        EasyExcel.read(inputStream, listener)
                .sheet()
                .doRead();

        targetUpload.changeStatus(new TargetCompletedStatus());
        targetUpload.applySendRequest(request);
    }
}
