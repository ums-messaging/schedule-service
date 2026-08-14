package com.ums.schedule.fixture.target;

import com.ums.schedule.application.sendrequest.target.data.TargetMessageData;
import com.ums.schedule.application.ums.common.target.result.TargetMessageResult;
import com.ums.schedule.common.code.target.SendTargetRowStatus;
import com.ums.schedule.domain.target.TargetMessage;

public class TargetMessageResultBuilder {
    private Integer partitionNo;
    private Integer rowNo;
    private SendTargetRowStatus status;
    private String resultMessage;
    private TargetMessage targetMessage;
    private TargetMessageData targetMessageData;

    public static TargetMessageResultBuilder builder() {
        return new TargetMessageResultBuilder();
    }

    private TargetMessageResultBuilder() {
        this.partitionNo = 3;
        this.rowNo = 2;
        this.status = SendTargetRowStatus.SUCCESS;
    }

    public TargetMessageResultBuilder partitionNo(Integer partitionNo) {
        this.partitionNo = partitionNo;
        return this;
    }

    public TargetMessageResultBuilder rowNo(Integer rowNo) {
        this.rowNo = rowNo;
        return this;
    }

    public TargetMessageResultBuilder status(SendTargetRowStatus status) {
        this.status = status;
        return this;
    }

    public TargetMessageResultBuilder targetMessage(TargetMessage targetMessage) {
        this.targetMessage = targetMessage;
        return this;
    }

    public TargetMessageResultBuilder targetMessageData(TargetMessageData targetMessageData) {
        this.targetMessageData = targetMessageData;
        return this;
    }

    public TargetMessageResult build() {
        return new TargetMessageResult(
                partitionNo,
                rowNo,
                status,
                resultMessage,
                targetMessage,
                targetMessageData
        );
    }
}
