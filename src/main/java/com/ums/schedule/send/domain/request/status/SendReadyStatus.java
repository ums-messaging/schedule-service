package com.ums.schedule.send.domain.request.status;

import com.ums.schedule.send.code.SendRequestStatusEnum;
import com.ums.schedule.send.code.SendTargetStatusEnum;
import com.ums.schedule.send.domain.request.SendRequest;
import com.ums.schedule.send.domain.request.status.exception.SendReadyStatusException;
import com.ums.schedule.send.domain.target.SendTarget;
import com.ums.schedule.send.domain.target.status.SendTargetStatus;

import java.util.List;

import static com.ums.schedule.send.code.SendRequestStatusEnum.*;

public class SendReadyStatus implements SendRequestStatus {

    @Override
    public SendRequestStatus toRequest() {
        return new RequestStatus();
    }

    @Override
    public SendRequestStatusEnum currentSendRequestStatus() {
        return READY;
    }
}
