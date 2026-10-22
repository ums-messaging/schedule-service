package com.ums.schedule.email.application.target.reader.listener.model;

import java.util.Map;

public record FailureSendTarget(
        Integer rowNo,
        Map<String, Object> targetData,
        String errorMessage
) {
    public static FailureSendTarget of(int rowNo, Map<String, Object> targetData, String errorMessage) {
        return new FailureSendTarget(
                rowNo,
                targetData,
                errorMessage
        );
    }
}
