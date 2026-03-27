package com.ums.schedule.send.domain.request.status;

import com.ums.schedule.send.code.SendRequestStatusEnum;
import com.ums.schedule.send.domain.request.status.exception.*;

public interface SendRequestStatus  {
    default SendRequestStatus toCreate() {
        throw SendReadyStatusException.of(currentSendRequestStatus().code());
    }

    default SendRequestStatus toRequest() {
        throw SendRequestStatusException.of(currentSendRequestStatus().code());
    }

    default SendRequestStatus toReady() {
        throw SendReadyStatusException.of(currentSendRequestStatus().code());
    }

    default SendRequestStatus toScheduled() {
        throw SchedulingStatusException.of(currentSendRequestStatus().code());
    }

    default SendRequestStatus toSend() {
        throw SendingStatusException.of(currentSendRequestStatus().code());
    }

    default SendRequestStatus toComplete() {
        throw SendCompletedStatusException.of(currentSendRequestStatus().code());
    }

    default SendRequestStatus toError() {
        return new SendFailStatus();
    }

    SendRequestStatusEnum currentSendRequestStatus();
}
