package com.ums.schedule.fixture.target;

import com.ums.schedule.application.target.reader.model.TargetUploadRowResult;
import com.ums.schedule.application.ums.common.target.context.SendTargetGroupedList;

import java.util.List;

public class SendTargetGroupedListBuilder {
    private Integer partitionNo;
    private Long groupId;
    private List<TargetUploadRowResult> targetGroupedList;

    public static SendTargetGroupedListBuilder builder() {
        return new SendTargetGroupedListBuilder();
    }

    public SendTargetGroupedListBuilder partitionNo(Integer partitionNo) {
        this.partitionNo = partitionNo;
        return this;
    }

    public SendTargetGroupedListBuilder targetGroupedList(List<TargetUploadRowResult> targetGroupedList) {
        this.targetGroupedList = targetGroupedList;
        return this;
    }

    public SendTargetGroupedList build() {
        return new SendTargetGroupedList(
                groupId,
                partitionNo,
                targetGroupedList
        );
    }
}
