package com.ums.schedule.send.application.model.command;

import com.ums.schedule.send.application.model.dto.TargetDataTransfer;
import com.ums.schedule.send.code.TargetColumnEnum;

import java.util.Map;

public record SendTargetCreateCommand(
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
               TargetColumnEnum.TARGET_KEY, targetKey,
               TargetColumnEnum.TARGET_NAME, targetName,
               TargetColumnEnum.TARGET_EMAIL, email,
               TargetColumnEnum.TARGET_PHONE, phoneNumber,
               TargetColumnEnum.TARGET_BIRTHDAY, birthday
        );
    }

    @Override
    public Map<String, Object> extractMessageVariable() {
        return this.messageVariable;
    }
}
