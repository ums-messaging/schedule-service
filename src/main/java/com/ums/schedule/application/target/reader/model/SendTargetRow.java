package com.ums.schedule.application.target.reader.model;

import com.ums.schedule.application.sendrequest.target.data.TargetMessageData;
import com.ums.schedule.common.code.target.SendTargetColumn;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public record SendTargetRow (
        Integer rowNo,
        Map<Integer, String> headMap,
        Map<Long, String> targetData
) {

    public static SendTargetRow of(Integer rowNo, Map<Integer, String> headMap, Map<Long, String> targetData) {
        return new SendTargetRow(rowNo, headMap, targetData);
    }

    public TargetMessageData toTargetData(String customerId) {
        Map<String, Object> messageData = extractMessageVariable();
        return TargetMessageData.of(rowNo,customerId, messageData);
    }

    public Map<String, Object> extractMessageVariable() {
        Map<String, Object> dataParam = headMap.entrySet()
                .stream()
                .filter(entry -> isMessageVariable(entry.getValue()))
                .collect(Collectors.toMap(
                        entry -> entry.getValue(),
                        entry -> this.targetData.get(entry.getKey()),
                        (oldVal, newVal) -> newVal));

        return dataParam;
    }

    private Boolean isMessageVariable(String value) {
        return Arrays.stream(SendTargetColumn.class.getEnumConstants())
                .filter(col -> ! value.equals(col.value()))
                .findFirst()
                .map(col -> true)
                .orElse(false);
    }
}
