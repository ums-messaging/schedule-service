package com.ums.schedule.domain.target.event;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ums.schedule.domain.target.state.upload.TargetUploadState;

public interface TargetUploadEvent {
    @JsonIgnore
    TargetUploadState getToStatus();
}
