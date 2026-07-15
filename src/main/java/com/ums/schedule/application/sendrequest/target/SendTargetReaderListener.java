package com.ums.schedule.application.sendrequest.target;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.ums.schedule.adapter.api.target.context.TargetUploadContext;
import com.ums.schedule.application.sendrequest.target.data.TargetMessageData;
import com.ums.schedule.application.sendrequest.target.row.SendTargetRow;
import com.ums.schedule.domain.request.target.upload.TargetUploadReport;
import org.springframework.context.ApplicationEventPublisher;

import java.util.*;

public class SendTargetReaderListener extends AnalysisEventListener<Map<Long, String>> {
    private final List<TargetMessageData> targetList = new ArrayList<>();

    private final int BATCH_SIZE = 1000;

    private final TargetUploadReport targetUpload;
    private final ApplicationEventPublisher publisher;
    private Map<Integer, String> headMap;

    public SendTargetReaderListener(TargetUploadReport targetUpload, ApplicationEventPublisher publisher) {
        this.targetUpload = targetUpload;
        this.publisher = publisher;
    }

    @Override
    public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
        this.headMap = headMap;
    }

    @Override
    public void invoke(Map<Long, String> targetData, AnalysisContext context) {
        SendTargetRow row = SendTargetRow.of(context.readRowHolder().getRowIndex()+1, headMap, targetData);
        targetList.add(row.toTargetData());

        if (targetList.size() >= BATCH_SIZE) {
            publishEvent();
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        if(!targetList.isEmpty()) {
            publishEvent();
        }
    }

    private void publishEvent() {
        TargetUploadContext targetUploadContext = TargetUploadContext.of(targetUpload, targetList);
        publisher.publishEvent(targetUploadContext);
    }
}