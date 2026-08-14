package com.ums.schedule.application.ums.common.target.context;

import com.ums.schedule.application.sendrequest.target.data.TargetMessageData;
import com.ums.schedule.application.target.reader.model.TargetRowResult;
import com.ums.schedule.application.ums.common.target.result.TargetMessageResult;
import com.ums.schedule.common.code.mapper.EnumMapperValue;
import com.ums.schedule.common.code.target.SendTargetRowStatus;
import com.ums.schedule.domain.target.TargetMessage;
import com.ums.schedule.domain.target.state.SendTargetFailState;
import com.ums.schedule.domain.target.state.SendTargetState;

public record SendTargetCreateContext(
   Integer partitionNo,
   Integer rowNo,
   SendTargetRowStatus state,
   String resultMessage,
   TargetMessage targetMessage,
   TargetMessageData targetMessageData

) {

    public static SendTargetCreateContext of(TargetMessageResult result) {
        return new SendTargetCreateContext(
                result.partitionNo(),
                result.rowNo(),
                result.status(),
                result.resultMessage(),
                result.targetMessage(),
                result.targetMessageData()
        );
    }

    public static SendTargetCreateContext of(TargetMessageResult result, String errorMessage) {
        return new SendTargetCreateContext(
                result.partitionNo(),
                result.rowNo(),
                result.status(),
                errorMessage,
                result.targetMessage(),
                result.targetMessageData()
        );
    }
}
