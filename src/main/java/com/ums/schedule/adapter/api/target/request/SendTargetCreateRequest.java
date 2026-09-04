package com.ums.schedule.adapter.api.target.request;

import com.ums.schedule.application.sendrequest.target.data.TargetMessageData;
import com.ums.schedule.common.code.target.SendTargetColumn;

import java.util.Map;

public record SendTargetCreateRequest(
        String targetKey,
        String targetName,
        String email,
        String phoneNumber,
        String birthday,
        Map<String, Object> messageVariable
)  {


    public Map<SendTargetColumn, String> resolveTargetData() {
        return Map.of(
                SendTargetColumn.TARGET_KEY, targetKey,
                SendTargetColumn.TARGET_NAME, targetName,
                SendTargetColumn.TARGET_EMAIL, email,
                SendTargetColumn.TARGET_PHONE, phoneNumber,
                SendTargetColumn.TARGET_BIRTHDAY, birthday
        );
    }
}