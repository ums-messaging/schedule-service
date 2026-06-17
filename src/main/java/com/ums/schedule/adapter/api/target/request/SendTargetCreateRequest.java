package com.ums.schedule.adapter.api.target.request;

import com.ums.schedule.application.sendrequest.target.data.TargetMessageData;
import com.ums.schedule.domain.sendrequest.target.code.TargetColumnEnum;

import java.util.Map;

public record SendTargetCreateRequest(
        String targetKey,
        String targetName,
        String email,
        String phoneNumber,
        String birthday,
        Map<String, Object> messageVariable
)  {

    public TargetMessageData toTargetData() {
        Map<TargetColumnEnum, String> targetData = resolveTargetData();
        return new TargetMessageData(targetData, this.messageVariable);
    }

    public Map<TargetColumnEnum, String> resolveTargetData() {
        return Map.of(
               TARGET_KEY, targetKey,
               TARGET_NAME, targetName,
               TARGET_EMAIL, email,
               TARGET_PHONE, phoneNumber,
               TARGET_BIRTHDAY, birthday
        );
    }
}
