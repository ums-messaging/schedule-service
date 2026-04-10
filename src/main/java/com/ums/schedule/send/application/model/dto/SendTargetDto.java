package com.ums.schedule.send.application.model.dto;

import com.ums.schedule.send.code.TargetColumnEnum;

import java.util.Map;

public record SendTargetDto(
        String uploadId,
        Map<TargetColumnEnum, String> targetData,
        Map<String, Object> dataParam
) {

    public static SendTargetDto of(TargetDataTransfer targetDto, String uploadId) {
        Map<TargetColumnEnum, String> targetData = targetDto.resolveTargetData();
        Map<String, Object> param = targetDto.extractMessageVariable();
        SendTargetDto dto = new SendTargetDto(uploadId, targetData, param);
        dto.putTargetData();
        return dto;
    }

    private void putTargetData() {
        for (Map.Entry<TargetColumnEnum, String> entry : targetData.entrySet()) {
            dataParam.put(entry.getKey().value(), entry.getValue());
        }
    }
}