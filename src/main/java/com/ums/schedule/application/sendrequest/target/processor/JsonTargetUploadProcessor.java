package com.ums.schedule.application.sendrequest.target.processor;

import com.ums.schedule.adapter.api.request.SendRequestCreateRequest;
import com.ums.schedule.application.sendrequest.data.SendRequestKeyData;
import com.ums.schedule.application.sendrequest.target.SendTargetUploadService;
import com.ums.schedule.application.sendrequest.target.result.TargetUploadResult;
import com.ums.schedule.common.code.mapper.EnumMapperValue;
import com.ums.schedule.domain.sendrequest.target.upload.code.TargetUploadTypeEnum;
import com.ums.schedule.domain.sendrequest.target.upload.TargetUploadReport;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.ums.schedule.domain.sendrequest.target.upload.code.TargetUploadTypeEnum.JSON;

@Component
@RequiredArgsConstructor
public class JsonTargetUploadProcessor implements TargetUploadProcessor {
    private final SendTargetUploadService targetUploadService;

    @Override
    public boolean supports(EnumMapperValue mapperValue) {
        return JSON == TargetUploadTypeEnum.valueOf(mapperValue.code());
    }

    @Override
    public TargetUploadResult requestUpload(TargetUploadReport targetUpload, SendRequestCreateRequest request, String messageId) {
        SendRequestKeyData keyData = SendRequestKeyData.of(messageId, targetUpload);
        targetUploadService.upload(targetUpload, keyData, request.toSendTargetDtos(), 1000);
        return TargetUploadResult.of(targetUpload);
    }
}
