package com.ums.schedule.application.target.dto;

import com.ums.schedule.code.send.TargetColumnEnum;
import com.ums.schedule.domain.channel.ChannelTemplate;

import java.util.Map;

public record SendTargetDto(
        Map<TargetColumnEnum, String> targetData,
        Map<String, Object> dataParam
) {

    public static SendTargetDto of(TargetDataTransfer targetDto) {
        Map<TargetColumnEnum, String> targetData = targetDto.resolveTargetData();
        Map<String, Object> param = targetDto.extractMessageVariable();
        SendTargetDto dto = new SendTargetDto(targetData, param);
//        dto.putTargetData();
        return dto;
    }

    private void putTargetData() {
        for (Map.Entry<TargetColumnEnum, String> entry : targetData.entrySet()) {
            dataParam.put(entry.getKey().value(), entry.getValue());
        }
    }
}