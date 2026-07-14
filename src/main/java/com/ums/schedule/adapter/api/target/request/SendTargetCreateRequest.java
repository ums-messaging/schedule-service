package com.ums.schedule.adapter.api.target.request;

import com.ums.schedule.application.sendrequest.target.data.TargetMessageData;
import com.ums.schedule.common.code.target.TargetColumnEnum;

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
                TargetColumnEnum.TARGET_KEY, targetKey,
                TargetColumnEnum.TARGET_NAME, targetName,
                TargetColumnEnum.TARGET_EMAIL, email,
                TargetColumnEnum.TARGET_PHONE, phoneNumber,
                TargetColumnEnum.TARGET_BIRTHDAY, birthday
        );
    }
}