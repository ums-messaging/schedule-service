package com.ums.schedule.send.application.resolver;

import com.ums.schedule.common.code.EnumMapperValue;
import com.ums.schedule.send.application.model.command.SendRequestCommand;
import com.ums.schedule.send.code.SendRequestEnumMapper;
import com.ums.schedule.send.code.TargetUploadTypeEnum;
import com.ums.schedule.send.domain.request.SendRequest;
import com.ums.schedule.send.domain.target.upload.TargetUpload;
import org.springframework.stereotype.Component;

import static com.ums.schedule.common.code.EnumMapperValue.fromEnumMapperType;
import static com.ums.schedule.send.code.TargetUploadTypeEnum.FILE;
import static com.ums.schedule.send.code.TargetUploadTypeEnum.JSON;

@Component
public class SendRequestFileResolver implements SendRequestResolver {
    @Override
    public boolean supports(EnumMapperValue uploadType) {
        return FILE.code().equals(uploadType.code());
    }

    @Override
    public TargetUpload resolve(SendRequest sendRequest) {
        EnumMapperValue uploadType = EnumMapperValue.fromEnumMapperType(FILE);
        TargetUpload targetUpload = TargetUpload.of(uploadType);
        targetUpload.applySendRequest(sendRequest);
        // targetUpload.fromResponse(); awsS3 Response

        return targetUpload;
    }
}
