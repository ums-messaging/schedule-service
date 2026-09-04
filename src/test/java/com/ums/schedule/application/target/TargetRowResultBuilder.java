package com.ums.schedule.application.target;

import com.ums.schedule.application.sendrequest.target.data.TargetMessageData;
import com.ums.schedule.application.target.reader.model.TargetUploadRowResult;
import com.ums.schedule.common.code.target.SendTargetResultCode;
import com.ums.schedule.common.code.target.SendTargetRowStatus;

public class TargetRowResultBuilder {
    private Integer rowNo;
    private String groupKey;
    private TargetMessageData targetMessage;
    private SendTargetResultCode resultCode;
    private String resultMessage;

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

    public TargetRowResultBuilder resultCode(SendTargetResultCode resultCode) {
        this.resultCode = resultCode;
        return this;
    }

    public TargetRowResultBuilder resultMessage(String resultMessage) {
        this.resultMessage = resultMessage;
        return this;
    }

    public TargetUploadRowResult build() {
        return new TargetUploadRowResult(
                rowNo,
                groupKey,
                targetMessage,
                resultCode,
                resultMessage
        );
    }
}
