package com.ums.schedule.common.util;

import org.springframework.util.StringUtils;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class TemplateUtil {
    public static String parse(String content, Map<String, Object> dataParamMap) {
        if(!StringUtils.hasText(content)) {
            return null;
        }
        Set<String> keySet = getKeySet(content);
        for(String key : keySet) {
            Object value = dataParamMap.get(key);
            if(value == null) {
                return null;
            }
            String valueTo = String.valueOf(value);
            content = content.replace("${".concat(key).concat("}"), valueTo);
        }
        return content;
    }

    private static Set<String> getKeySet(String content) {
        Pattern pattern = Pattern.compile("\\$\\{([^}]+)\\}");
        Matcher matcher = pattern.matcher(content);
        Set<String> keySet = new HashSet<>();
        while(matcher.find()) {
            keySet.add(matcher.group(1));
        }
        return keySet;
    }
}
