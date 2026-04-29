package com.ums.schedule.domain.target.event;

import com.ums.schedule.application.target.dto.SendTargetDto;
import com.ums.schedule.domain.channel.ChannelTemplate;
import com.ums.schedule.domain.target.upload.TargetUpload;
import com.ums.schedule.domain.target.state.upload.TargetUploadParsingState;
import com.ums.schedule.domain.target.state.upload.TargetUploadState;

import java.util.List;

public record TargetMessageCreatedEvent(

) implements TargetUploadEvent {

    public static TargetMessageCreatedEvent of(TargetUpload targetUpload, ChannelTemplate template, List<SendTargetDto> targetDtos){
        TargetMessageCreatedEvent event = new TargetMessageCreatedEvent();
        return event;
    }

    @Override
    public TargetUploadState getToStatus() {
        return new TargetUploadParsingState();
    }
}
