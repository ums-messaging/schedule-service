package com.ums.schedule.application.target.reader.model;

import com.ums.schedule.application.sendrequest.target.data.TargetMessageData;
import com.ums.schedule.common.code.target.SendTargetResultCode;

public record TargetUploadRowResult(
        Integer rowNo,
        String groupKey,
        TargetMessageData targetMessage,
        SendTargetResultCode resultCode,
        String resultMessage
) {
    public static TargetUploadRowResult of(SendTargetRow row, String customerId, String groupKey) {
        return new TargetUploadRowResult(
                row.rowNo(),
                groupKey,
                row.toTargetData(customerId),
                SendTargetResultCode.SUCCESS,
                null
        );
    }

    public static TargetUploadRowResult of(Integer rowNo, SendTargetResultCode resultCode, String... args) {
        return new TargetUploadRowResult(
                rowNo,
                null,
                null,
                resultCode,
                String.format(resultCode.description(), args)
        );
    }
}
