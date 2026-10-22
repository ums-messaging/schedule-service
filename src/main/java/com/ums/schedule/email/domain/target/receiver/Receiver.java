package com.ums.schedule.email.domain.target.receiver;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.Optional;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class Receiver {
    private String targetKey;
    private String name;
    private String email;
    
    public static Receiver of(Map<String, Object> targetData) {
        Receiver receiver = new Receiver();
        receiver.assignTargetKey(targetData);
        receiver.assignTargetName(targetData);
        receiver.assignTargetEmail(targetData);
        return receiver;
    }

    private void assignTargetEmail(Map<String, Object> targetData) {
        this.email = getDataString("email", targetData);
    }

    private void assignTargetName(Map<String, Object> targetData) {
        this.name = getDataString("name", targetData);
    }

    private void assignTargetKey(Map<String, Object> targetData) {
        this.targetKey = getDataString("key", targetData);
    }


    private String getDataString(String key, Map<String, Object> targetData) {
        return Optional.ofNullable(targetData.get(key))
                .map(String::valueOf)
                .filter(StringUtils::hasText)
                .orElseThrow();
    }
    
    
}
