package com.ums.schedule.domain.sendrequest.target.event;

import com.ums.schedule.application.sendrequest.target.data.TargetMessageData;
import com.ums.schedule.domain.sendrequest.template.ChannelTemplate;
import com.ums.schedule.domain.sendrequest.target.upload.TargetUploadReport;
import com.ums.schedule.domain.sendrequest.target.upload.state.TargetUploadParsingState;
import com.ums.schedule.domain.sendrequest.target.upload.state.TargetUploadState;

import java.util.List;

public record TargetMessageCreatedEvent(

) implements TargetUploadEvent {

    public static TargetMessageCreatedEvent of(TargetUploadReport targetUpload, ChannelTemplate template, List<TargetMessageData> targetDtos){
        TargetMessageCreatedEvent event = new TargetMessageCreatedEvent();
        return event;
    }

    @Override
    public TargetUploadState getToStatus() {
        return new TargetUploadParsingState();
    }
}
