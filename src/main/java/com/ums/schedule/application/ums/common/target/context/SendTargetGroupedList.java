package com.ums.schedule.application.ums.common.target.context;

import com.github.f4b6a3.tsid.TsidCreator;
import com.ums.schedule.application.target.reader.model.TargetUploadRowResult;

import java.util.List;

public record SendTargetGroupedList(
        Long groupId,
        Integer partitionNo,
        List<TargetUploadRowResult> targetRows
) {
    public static SendTargetGroupedList of(Integer partitionNo, List<TargetUploadRowResult> targetGroupedList) {
        return new SendTargetGroupedList(
                TsidCreator.getTsid().toLong(),
                partitionNo,
                targetGroupedList
        );
    }
}
