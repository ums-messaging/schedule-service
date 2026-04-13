package com.ums.schedule.adapter.api.target;

import com.ums.schedule.application.target.dto.TargetDataTransfer;
import com.ums.schedule.code.send.TargetColumnEnum;

import java.util.Map;

import static com.ums.schedule.code.send.TargetColumnEnum.*;
import static com.ums.schedule.code.send.TargetColumnEnum.TARGET_KEY;

public record SendTargetUploadRequest(
        String targetKey,
        String targetName,
        String email,
        String phoneNumber,
        String birthday,
        Map<String, Object> messageVariable
) implements TargetDataTransfer {

    @Override
    public Map<TargetColumnEnum, String> resolveTargetData() {
        return Map.of(
               TARGET_KEY, targetKey,
               TARGET_NAME, targetName,
               TARGET_EMAIL, email,
               TARGET_PHONE, phoneNumber,
               TARGET_BIRTHDAY, birthday
        );
    }

    @Override
    public Map<String, Object> extractMessageVariable() {
        return this.messageVariable;
    }
}
