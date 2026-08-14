package com.ums.schedule.fixture.target;

import com.ums.schedule.application.target.reader.model.TargetRowResult;
import com.ums.schedule.application.ums.common.target.context.SendTargetGroupedListContext;

import java.util.List;

public class SendTargetGroupedListContextBuilder {
    private Integer partitionNo;
    private List<TargetRowResult> targetGroupedList;

    public static SendTargetGroupedListContextBuilder builder() {
        return new SendTargetGroupedListContextBuilder();
    }

    public SendTargetGroupedListContextBuilder partitionNo(Integer partitionNo) {
        this.partitionNo = partitionNo;
        return this;
    }

    public SendTargetGroupedListContextBuilder targetGroupedList(List<TargetRowResult> targetGroupedList) {
        this.targetGroupedList = targetGroupedList;
        return this;
    }

    public SendTargetGroupedListContext build() {
        return new SendTargetGroupedListContext(
                partitionNo,
                targetGroupedList
        );
    }
}
