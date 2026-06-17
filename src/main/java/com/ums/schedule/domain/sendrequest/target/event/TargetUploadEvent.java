package com.ums.schedule.domain.sendrequest.target.event;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ums.schedule.domain.sendrequest.target.upload.state.TargetUploadState;

public interface TargetUploadEvent {
    @JsonIgnore
    TargetUploadState getToStatus();
}
