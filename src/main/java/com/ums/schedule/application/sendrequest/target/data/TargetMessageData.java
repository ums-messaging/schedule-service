package com.ums.schedule.application.sendrequest.target.data;

import com.ums.schedule.common.code.target.SendTargetColumn;
import com.ums.schedule.common.code.target.SendTargetResultCode;
import com.ums.schedule.domain.target.TargetMessage;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public record TargetMessageData(
        String customerId,
        Integer rowNo,
        Map<SendTargetColumn, String> targetData,
        Map<String, Object> dataParam
) {
    public static TargetMessageData of(Integer rowNo, String customerId, Map<String, Object> dataParam) {
        Map<SendTargetColumn, String> targetData = Arrays.stream(SendTargetColumn.class.getEnumConstants())
                .filter(col -> dataParam.containsKey(col.value()))
                .collect(Collectors.toMap(
                        col -> col,
                        col -> String.valueOf(dataParam.get(col.value()))
                ));

        return new TargetMessageData(customerId, rowNo, targetData, dataParam);
    }

    public Map<String, Object> getTargetParam() {
        Map<String, Object> targetParamMap = new HashMap<>();

        Map<String, String> targetData = this.targetData.entrySet()
                .stream()
                .collect(Collectors.toMap(entry -> entry.getKey().value(), Map.Entry::getValue));

        targetParamMap.putAll(targetData);
        targetParamMap.putAll(dataParam);

        return targetParamMap;
    }

    public String targetKey() {
        return targetData.get(SendTargetColumn.TARGET_KEY);
    }

    public String getString(String key) {
        if(StringUtils.hasText(key)) {
            return String.valueOf(dataParam.get(key));
        }
        return null;
    }

}