package com.ums.schedule.application.ums.common.target.result;

import com.ums.schedule.application.sendrequest.target.data.TargetMessageData;
import com.ums.schedule.application.target.reader.model.TargetUploadRowResult;
import com.ums.schedule.application.ums.common.target.context.SendTargetGroupedList;
import com.ums.schedule.common.code.target.SendTargetResultCode;

public record SendTargetResult(
        Integer partitionNo,
        Long groupId,
        String groupKey,
        Integer rowNo,
        SendTargetResultCode resultCode,
        String resultMessage,
        TargetMessageData targetData
        ) {
    public static SendTargetResult of(SendTargetGroupedList group, TargetUploadRowResult row) {
        return new SendTargetResult(
                group.partitionNo(),
                group.groupId(),
                row.groupKey(),
                row.rowNo(),
                row.resultCode(),
                row.resultMessage(),
                row.targetMessage()
        );
    }


    public static SendTargetResult of(SendTargetResult result, SendTargetResultCode resultCode, String errorMessage) {
        String resultMessage = String.format(resultCode.description(), errorMessage);
        return new SendTargetResult(
                result.partitionNo(),
                result.groupId(),
                result.groupKey(),
                result.rowNo(),
                resultCode,
                resultMessage,
                result.targetData()
        );
    }
}
