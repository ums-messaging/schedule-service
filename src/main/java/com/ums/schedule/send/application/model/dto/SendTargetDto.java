package com.ums.schedule.send.application.model.dto;

import com.ums.schedule.send.application.model.command.SendTargetCreateCommand;
import com.ums.schedule.send.code.SendTargetStatusEnum;
import com.ums.schedule.send.code.TargetColumnEnum;
import com.ums.schedule.send.code.TargetUploadStatusEnum;
import com.ums.schedule.send.domain.request.SendRequest;
import com.ums.schedule.send.domain.target.SendTarget;
import lombok.Getter;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.ums.schedule.send.code.TargetColumnEnum.*;


public record SendTargetDto(
        Long rowNo,
        Map<Integer, String> headMap,
        Map<Long, String> targetData
) {
    public static SendTargetDto of(Integer rowNo, Map<Integer, String> headMap, Map<Long, String> targetData) {
        return new SendTargetDto((long) rowNo, headMap, targetData);
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
        putTargetData(dataParam);
        return dataParam;
    }

    private void putTargetData(Map<String, Object> dataParam) {
        Map<TargetColumnEnum, String> targetData = resolveTargetData();
        for (Map.Entry<TargetColumnEnum, String> entry : targetData.entrySet()) {
            dataParam.put(entry.getKey().value(), entry.getValue());
        }
    }

    private Boolean isMessageVariable(String value) {
        return Arrays.stream(TargetColumnEnum.class.getEnumConstants())
                .filter(col -> !value.equals(col.value()))
                .findFirst()
                .map(col -> true)
                .orElse(false);
    }

    public String parse(String content) {
        Map<String, Object> targetDataParam = extractMessageVariable();
        Set<String> keySet = getKeySet(content);
        for(String key : keySet) {
            String value = (String) targetDataParam.getOrDefault(key, null);
            if(value == null) {
                throw new RuntimeException();
            }
            content = content.replace("#{".concat(key).concat("}"), value);
        }
        return content;
    }

    private Set<String> getKeySet(String content) {
        Pattern pattern = Pattern.compile("#\\{([^}]+)\\}");
        Matcher matcher = pattern.matcher(content);
        Set<String> keySet = getKeySet(content);
        while(matcher.find()) {
            keySet.add(matcher.group(1));
        }
        return keySet;
    }
}
