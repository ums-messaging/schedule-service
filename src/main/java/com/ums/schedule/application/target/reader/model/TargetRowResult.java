package com.ums.schedule.application.target.reader.model;

import com.ums.schedule.application.sendrequest.target.data.TargetMessageData;
import com.ums.schedule.common.code.target.SendTargetRowStatus;

public record TargetRowResult(
        Integer rowNo,
        TargetMessageData targetMessage,
        SendTargetRowStatus status,
        String reason
) {
    public static TargetRowResult of(SendTargetRow row) {
        return new TargetRowResult(
                row.rowNo(),
                row.toTargetData(),
                SendTargetRowStatus.SUCCESS,
                null
        );
    }

    public static TargetRowResult of(Integer rowNo, String errorMessage) {
        return new TargetRowResult(
                rowNo,
                null,
                SendTargetRowStatus.FAIL,
                errorMessage
        );
    }
}
