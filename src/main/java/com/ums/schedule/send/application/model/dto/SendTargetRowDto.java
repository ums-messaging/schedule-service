package com.ums.schedule.send.application.model.dto;

import com.ums.schedule.send.code.TargetColumnEnum;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
public record SendTargetRowDto(
        Long rowNo,
        Map<Integer, String> headMap,
        Map<Long, String> targetData
) implements TargetDataTransfer {

    public static SendTargetRowDto of(Integer rowNo, Map<Integer, String> headMap, Map<Long, String> targetData) {
        return new SendTargetRowDto((long) rowNo, headMap, targetData);
    }

    @Override
    public Map<TargetColumnEnum, String> resolveTargetData() {
        return Arrays.stream(TargetColumnEnum.class.getEnumConstants())
                .filter(col -> headMap.containsValue(col.value()))
                .collect(Collectors.toMap(
                        col -> col,
                        col -> targetData.get(col.value()),
                        (oldVal, newVal) -> newVal
                ));
    }
    @Override
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
