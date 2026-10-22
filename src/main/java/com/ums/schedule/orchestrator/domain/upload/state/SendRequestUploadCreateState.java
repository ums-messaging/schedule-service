package com.ums.schedule.orchestrator.domain.upload.state;

import com.ums.schedule.common.code.target_upload.TargetUploadEvent;
import com.ums.schedule.orchestrator.common.code.SendRequestUploadStatus;
import com.ums.schedule.common.converter.state.StatusStateEvent;
import com.ums.schedule.orchestrator.domain.upload.exception.InvalidTargetUploadStateException;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class SendRequestUploadCreateState implements SendRequestUploadState {

    @Override
    public SendRequestUploadState onEvent(StatusStateEvent event) {
        TargetUploadEvent eventCode = TargetUploadEvent.valueOf(event.code());
        switch (eventCode) {
            case PROVIDED_URL -> {
                return new SendRequestUploadWaitingState();
            }
        }
        throw InvalidTargetUploadStateException.of(SendRequestUploadStatus.WAITING, eventCode.stateType());
    }

    @Override
    public SendRequestUploadStatus getCurrentCode() {
        return SendRequestUploadStatus.WAITING;
    }
}