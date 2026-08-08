package com.ums.schedule.application.sendrequest.target.data;

import com.ums.schedule.common.code.target.SendTargetColumn;
import com.ums.schedule.domain.target.exception.SendTargetMessageVariableMissingException;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public record TargetMessageData(
        Map<SendTargetColumn, String> targetData,
        Map<String, Object> dataParam
) {


    public static TargetMessageData of(Map<String, Object> dataParam) {
        Map<SendTargetColumn, String> targetData = Arrays.stream(SendTargetColumn.class.getEnumConstants())
                .filter(col -> dataParam.containsKey(col.value()))
                .collect(Collectors.toMap(
                        col -> col,
                        col -> String.valueOf(dataParam.get(col.value()))
                ));

        return new TargetMessageData(targetData, dataParam);
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

    public String parse(String content) {
        if(!StringUtils.hasText(content)) {
            return null;
        }
        String targetKey = String.valueOf(dataParam.get(SendTargetColumn.TARGET_KEY.value()));
        Set<String> keySet = getKeySet(content);
        for(String key : keySet) {
            Object value = dataParam.get(key);
            if(value == null) {
                throw SendTargetMessageVariableMissingException.of(targetKey, key);
            }
            String valueTo = String.valueOf(value);
            content = content.replace("${".concat(key).concat("}"), valueTo);
        }
        return content;
    }

    private Set<String> getKeySet(String content) {
        Pattern pattern = Pattern.compile("\\$\\{([^}]+)\\}");
        Matcher matcher = pattern.matcher(content);
        Set<String> keySet = new HashSet<>();
        while(matcher.find()) {
            keySet.add(matcher.group(1));
        }
        return keySet;
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