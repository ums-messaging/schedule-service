package com.ums.schedule.application.ums.common.target.context;

import com.ums.schedule.application.target.reader.model.TargetRowResult;

import java.util.List;
import java.util.stream.Collectors;

public record SendTargetGroupedListContext(
        Integer partitionNo,
        List<TargetRowResult> targetGroupedList
) {
    public static SendTargetGroupedListContext of(Integer partitionNo, List<TargetRowResult> targetGroupedList) {
        return new SendTargetGroupedListContext(
                partitionNo,
                targetGroupedList
        );
    }
}
