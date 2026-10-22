package com.ums.schedule.email.application.target;

import com.ums.schedule.common.code.request.SendGroupEventType;
import com.ums.schedule.email.domain.event.DomainGroupEvent;
import com.ums.schedule.email.domain.event.SendGroupEventResult;
import com.ums.schedule.email.domain.event.SendTargetGroupEvent;
import com.ums.schedule.email.domain.event.SendTargetResultList;
import com.ums.schedule.email.domain.target.SendTargetError;
import com.ums.schedule.email.domain.target.SendTargetRequest;

import java.util.List;

public record SendTargetUploadResult(
        List<SendTargetRequest> successList,
        List<SendTargetError> errorList
) {
    public static SendTargetUploadResult of(SendTargetResultList resultList) {
        return new SendTargetUploadResult(
                resultList.successList(),
                resultList.errorList());
    }
    public Integer totalCount() {
        return successList.size() + errorList.size();
    }

}
