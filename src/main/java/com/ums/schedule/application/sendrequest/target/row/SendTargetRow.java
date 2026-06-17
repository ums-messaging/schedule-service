package com.ums.schedule.application.sendrequest.target.row;

import com.ums.schedule.application.sendrequest.target.data.TargetMessageData;
import com.ums.schedule.domain.sendrequest.target.code.TargetColumnEnum;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public record SendTargetRow(
        Long rowNo,
        Map<Integer, String> headMap,
        Map<Long, String> targetData
)  {

    public static SendTargetRow of(Integer rowNo, Map<Integer, String> headMap, Map<Long, String> targetData) {
        return new SendTargetRow((long) rowNo, headMap, targetData);
    }

    public TargetMessageData toTargetData() {
        Map<TargetColumnEnum, String> targetData = resolveTargetData();
        Map<String, Object> messageData = extractMessageVariable();
        return new TargetMessageData(targetData, messageData);
    }

    public Map<TargetColumnEnum, String> resolveTargetData() {
        return Arrays.stream(TargetColumnEnum.class.getEnumConstants())
                .filter(col -> headMap.containsValue(col.value()))
                .collect(Collectors.toMap(
                        col -> col,
                        col -> targetData.get(col.value()),
                        (oldVal, newVal) -> newVal
                ));
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
        return Arrays.stream(TargetColumnEnum.class.getEnumConstants())
                .filter(col -> !value.equals(col.value()))
                .findFirst()
                .map(col -> true)
                .orElse(false);
    }
}
