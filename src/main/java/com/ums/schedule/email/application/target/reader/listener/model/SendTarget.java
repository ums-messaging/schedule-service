package com.ums.schedule.email.application.target.reader.listener.model;

import com.github.f4b6a3.uuid.UuidCreator;

import java.util.Map;

public record SendTarget(
        String groupKey,
        Integer row,
        String id,
        String key,
        String contact,
        String birthday,
        Map<String, Object> messageVariable
) {
    public static SendTarget of(int rowNo, String groupKey, Map<String, Object> targetData, String contact) {
        return new SendTarget(
                groupKey,
                rowNo,
                UuidCreator.getTimeOrdered().toString(),
                String.valueOf(targetData.get("key")),
                contact,
                String.valueOf(targetData.get("birthday")),
                targetData
        );
    }
}
