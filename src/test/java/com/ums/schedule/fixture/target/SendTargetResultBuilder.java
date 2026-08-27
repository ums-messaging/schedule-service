package com.ums.schedule.fixture.target;

import com.ums.schedule.application.sendrequest.target.data.TargetMessageData;
import com.ums.schedule.application.ums.common.target.result.SendTargetResult;
import com.ums.schedule.common.code.target.SendTargetResultCode;

public class SendTargetResultBuilder {
    private Integer partitionNo;
    private Long groupId;
    private String groupKey;
    private Integer rowNo;
    private SendTargetResultCode resultCode;
    private String resultMessage;
    private TargetMessageData targetData;

    public static SendTargetResultBuilder builder() {
        return new SendTargetResultBuilder();
    }
    private SendTargetResultBuilder() {
        this.partitionNo = 1;
        this.groupId = 1L;
        this.groupKey = "test.com";
        this.rowNo = 2;
        this.resultCode = SendTargetResultCode.SUCCESS;
        this.resultMessage = null;
    }

    public SendTargetResultBuilder partitionNo(Integer partitionNo) {
        this.partitionNo = partitionNo;
        return this;
    }

    public SendTargetResultBuilder groupId(Long groupId) {
        this.groupId = groupId;
        return this;
    }

    public SendTargetResultBuilder groupKey(String groupKey) {
        this.groupKey = groupKey;
        return this;
    }

    public SendTargetResultBuilder rowNo(Integer rowNo) {
        this.rowNo = rowNo;
        return this;
    }

    public SendTargetResultBuilder resultCode(SendTargetResultCode resultCode) {
        this.resultCode = resultCode;
        return this;
    }

    public SendTargetResultBuilder resultMessage(String resultMessage) {
        this.resultMessage = resultMessage;
        return this;
    }

    public SendTargetResultBuilder targetData(TargetMessageData targetData) {
        this.targetData = targetData;
        return this;
    }

    public SendTargetResult build() {
        return new SendTargetResult(
                partitionNo,
                groupId,
                groupKey,
                rowNo,
                resultCode,
                 resultMessage,
                targetData
        );
    }
}
