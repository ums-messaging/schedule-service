package com.ums.schedule.email.application.send.manager;

import com.ums.schedule.email.domain.target.SendTargetError;
import com.ums.schedule.email.domain.target.SendTargetRequest;

import java.util.List;

public record SendTargetSendResultList(
        List<SendTargetRequest> sentTargetList,
        List<SendTargetError> sendTargetErrorList
) {
    public static SendTargetSendResultList of(List<SendTargetRequest> sentTargetList,
                                              List<SendTargetError> sendTargetErrorList
                                              ) {
        return new SendTargetSendResultList(
                sentTargetList, sendTargetErrorList
        );
    }
}
