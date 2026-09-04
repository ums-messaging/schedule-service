package com.ums.schedule.fixture.target;

import com.ums.schedule.application.sendrequest.target.data.TargetMessageData;
import com.ums.schedule.common.code.target.SendTargetColumn;

import java.util.HashMap;
import java.util.Map;

public class TargetMessageDataBuilder {
    private String customerId;
    private Integer rowNo;
    private Map<SendTargetColumn, String> targetData;
    private Map<String, Object> dataParam;

    public static TargetMessageDataBuilder builder() {
        return new TargetMessageDataBuilder();
    }

    private TargetMessageDataBuilder() {
        this.customerId = "hyejin_company";
        this.rowNo = 1;
        this.targetData = Map.of(
                SendTargetColumn.TARGET_KEY, "hyejin",
                SendTargetColumn.TARGET_NAME, "jang",
                SendTargetColumn.TARGET_EMAIL, "jang314@test.com"
                );
        this.dataParam = new HashMap<>();
    }

    public TargetMessageDataBuilder customerId(String customerId) {
        this.customerId = customerId;
        return this;
    }

    public TargetMessageDataBuilder rowNo(Integer rowNo) {
        this.rowNo = rowNo;
        return this;
    }

    public TargetMessageDataBuilder targetData(Map<SendTargetColumn, String> targetData) {
        this.targetData = targetData;
        return this;
    }

    public TargetMessageDataBuilder dataParam(Map<String, Object> dataParam) {
        this.dataParam = dataParam;
        return this;
    }

    public TargetMessageData build() {
        return new TargetMessageData(
                customerId,
                rowNo,
                targetData,
                dataParam
        );
    }
}
