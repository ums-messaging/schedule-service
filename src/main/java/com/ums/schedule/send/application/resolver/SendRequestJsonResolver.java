package com.ums.schedule.send.application.resolver;

import com.ums.schedule.common.code.EnumMapperValue;
import com.ums.schedule.send.domain.request.SendRequest;
import com.ums.schedule.send.domain.request.upload.TargetUpload;

import static com.ums.schedule.common.code.EnumMapperValue.*;
import static com.ums.schedule.send.code.TargetUploadTypeEnum.*;

public class SendRequestJsonResolver implements SendRequestResolver {
    @Override
    public boolean supports(EnumMapperValue mapperValue) {
        return mapperValue.code().equals("FILE");
    }

    @Override
    public TargetUpload resolve(SendRequest sendRequest) {
        TargetUpload targetUpload = TargetUpload.of(fromEnumMapperType(JSON));
        targetUpload.applySendRequest(sendRequest);
        return targetUpload;
    }
}
