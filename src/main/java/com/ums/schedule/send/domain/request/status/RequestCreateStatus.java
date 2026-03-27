package com.ums.schedule.send.domain.request.status;

import com.ums.schedule.send.code.SendRequestStatusEnum;
import com.ums.schedule.send.code.SendTargetStatusEnum;
import com.ums.schedule.send.code.TargetUploadStatusEnum;
import com.ums.schedule.send.domain.request.SendRequest;
import com.ums.schedule.send.domain.request.status.exception.*;
import com.ums.schedule.send.domain.target.SendTarget;

import java.util.List;

import static com.ums.schedule.send.code.SendRequestStatusEnum.*;

public class RequestCreateStatus implements SendRequestStatus {

    @Override
    public SendRequestStatus toReady() {
        return new SendReadyStatus();
    }

    @Override
    public SendRequestStatusEnum currentSendRequestStatus() {
        return CREATE;
    }
}
