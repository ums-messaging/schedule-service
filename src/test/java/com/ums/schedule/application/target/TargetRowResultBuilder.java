package com.ums.schedule.application.target;

import com.ums.schedule.application.sendrequest.target.data.TargetMessageData;
import com.ums.schedule.application.target.reader.model.TargetRowResult;
import com.ums.schedule.common.code.target.SendTargetRowStatus;

public class TargetRowResultBuilder {
    private Integer rowNo;
    private TargetMessageData targetMessage;
    private SendTargetRowStatus status;
    private String reason;

    public static TargetRowResultBuilder builder() {
        return new TargetRowResultBuilder();
    }

    public TargetRowResultBuilder rowNo(Integer rowNo) {
        this.rowNo = rowNo;
        return this;
    }

    public TargetRowResultBuilder targetMessage(TargetMessageData targetMessage) {
        this.targetMessage = targetMessage;
        return this;
    }

    public TargetRowResultBuilder targetRowStatus(SendTargetRowStatus status) {
        this.status= status;
        return this;
    }

    public TargetRowResultBuilder reason(String reason) {
        this.reason = reason;
        return this;
    }

    public TargetRowResult build() {
        return new TargetRowResult(
                rowNo,
                targetMessage,
                status,
                reason
        );
    }
}
