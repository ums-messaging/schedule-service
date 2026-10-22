package com.ums.schedule.email.application.target.reader.listener.model;

import java.util.Map;
import java.util.stream.Collectors;

public record SendTargetReadRow(
        Integer rowNo,
        Map<Integer, String> headMap,
        Map<Long, String> targetData
) {

    public static SendTargetReadRow of(Integer rowNo, Map<Integer, String> headMap, Map<Long, String> targetData) {
        return new SendTargetReadRow(rowNo, headMap, targetData);
    }

    public Map<String, Object> getMessageVariable() {
        return headMap.entrySet()
                .stream()
                .collect(Collectors.toMap(
                        entry -> entry.getValue(),
                        entry -> this.targetData.get(entry.getKey()),
                        (oldVal, newVal) -> newVal));
    }

    private Integer getHeadNo(String headerName) {
        return headMap.entrySet()
                .stream()
                .filter(v -> v.getValue().equals(headerName))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElseGet(() -> -1);
    }

    public String readValue(String headerName) {
        Integer no = getHeadNo(headerName);
        if(no == -1) {
            return null;
        }
        return targetData.get(no);
    }
}
