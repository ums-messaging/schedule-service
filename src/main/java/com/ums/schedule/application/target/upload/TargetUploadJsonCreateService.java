package com.ums.schedule.application.target.upload;

import com.ums.schedule.code.EnumMapperValue;
import com.ums.schedule.code.send.TargetUploadTypeEnum;
import com.ums.schedule.domain.target.event.TargetUploadEvent;
import com.ums.schedule.domain.target.event.TargetUploadRequestedEvent;
import com.ums.schedule.domain.target.upload.TargetUpload;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.ums.schedule.code.send.TargetUploadTypeEnum.FILE;
import static com.ums.schedule.code.send.TargetUploadTypeEnum.JSON;

@Component
@RequiredArgsConstructor
public class TargetUploadJsonCreateService implements TargetUploadCreator {
    private final TargetUploadRequestService requestService;


    public TargetUpload create(TargetUploadCreateCommand command) {
        TargetUploadRequestedEvent event = TargetUploadRequestedEvent.of(JSON, null, null, command);
        TargetUpload targetUpload = requestService.create(event, command.targetDtoList());
        TargetUploadEvent sendEvent = targetUpload.createTargetUploadUrlEvent(null);

        return targetUpload;
    }

    @Override
    public boolean supports(EnumMapperValue mapperValue) {
        return FILE == TargetUploadTypeEnum.valueOf(mapperValue.code());
    }
}
