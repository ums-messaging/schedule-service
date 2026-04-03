package com.ums.schedule.send.application.reader;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.ums.schedule.message.domain.SendMessage;
import com.ums.schedule.send.application.model.dto.SendTargetDto;
import com.ums.schedule.send.application.model.dto.TargetRowDto;
import com.ums.schedule.send.application.service.SendTargetService;
import com.ums.schedule.send.code.TargetColumnEnum;
import com.ums.schedule.send.domain.request.SendRequest;

import java.util.*;
import java.util.stream.Collectors;

public class SendTargetReaderListener extends AnalysisEventListener<Map<Long, String>> {
    private final List<SendTargetDto> targetList = new ArrayList<>();
    private final List<SendTargetDto> targetFailures = new ArrayList<>();
    private final Map<String, String> targetKeyMap = new HashMap<>();
    private Map<Integer, String> headMap;
    private SendTargetService targetService;
    private SendRequest request;

    public SendTargetReaderListener(SendRequest request, SendTargetService targetService) {
        this.request = request;
        this.targetService = targetService;
    }

    @Override
    public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
        this.headMap = headMap;
    }

    @Override
    public void invoke(Map<Long, String> targetData, AnalysisContext context) {
        SendTargetDto targetDto = SendTargetDto.of(context.readRowHolder().getRowIndex(), headMap, targetData);


        if (targetList.size() % 10000 == 0) {
            targetService.saveList(request, targetList);
            this.targetList.clear();
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        // 나머지 큐로 빼기
        if(!targetList.isEmpty()) {
            targetService.saveList(request, targetList);
        }
    }
}
