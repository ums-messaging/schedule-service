package com.ums.schedule.application.ums.common.target.result;

import com.ums.schedule.application.sendrequest.target.data.TargetMessageData;
import com.ums.schedule.application.target.reader.model.TargetRowResult;
import com.ums.schedule.common.code.target.SendTargetRowStatus;
import com.ums.schedule.domain.target.TargetMessage;

public record TargetMessageResult(
        Integer partitionNo,
        Integer rowNo,
        SendTargetRowStatus status,
        String resultMessage,
        TargetMessage targetMessage,
        TargetMessageData targetMessageData
) {

    public static TargetMessageResult of(Integer partitionNo, TargetRowResult row, TargetMessage targetMessage) {
        return new TargetMessageResult(
                partitionNo,
                row.rowNo(),
                SendTargetRowStatus.SUCCESS,
                null,
                targetMessage,
                row.targetMessage()
        );
    }
    public static TargetMessageResult of(Integer partitionNo, TargetRowResult row, String errorMessage) {
        return new TargetMessageResult(
                partitionNo,
                row.rowNo(),
                SendTargetRowStatus.FAIL,
                errorMessage,
                null,
                row.targetMessage()
        );
    }
}
