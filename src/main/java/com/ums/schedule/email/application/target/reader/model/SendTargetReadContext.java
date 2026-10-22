package com.ums.schedule.email.application.target.reader.model;

import com.ums.schedule.email.application.target.reader.listener.model.SendTargetGroupedList;
import com.ums.schedule.email.application.target.reader.listener.model.SendTargetReadRow;
import com.ums.schedule.email.application.send.job.model.SendJob;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.function.Consumer;

@ToString
@Slf4j
@Getter(AccessLevel.PUBLIC)
public abstract class SendTargetReadContext {
    private final Long requestId;
    private final Integer batchSize;
    private final Integer partitionSize;
    private final Consumer<List<SendTargetGroupedList>> worker;

    private int groupCount = 0;
    private int totalCount = 0;
    private int successCount = 0;
    private int failCount = 0;

    public SendTargetReadContext(SendJob job, Consumer<List<SendTargetGroupedList>> handler) {
        this.requestId = job.sendRequestId();
        this.batchSize = job.batchSize();
        this.partitionSize = job.partitionSize();
        this.worker = handler;
    }

    public void incrementSuccessCount(int successCount) {
        this.successCount += successCount;
    }

    public void incrementFailCount(int failCount) {
        this.failCount += failCount;
    }
    public void incrementFailCount() {
        this.failCount++;
    }
    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public void setGroupCount(int groupCount) {
        this.groupCount = groupCount;
    }


    public abstract String getGroupKey(SendTargetReadRow row) ;

    public void incrementGroupCount() {
        groupCount++;
    }
}
