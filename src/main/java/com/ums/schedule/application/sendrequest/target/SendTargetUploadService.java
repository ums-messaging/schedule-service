package com.ums.schedule.application.sendrequest.target;

import com.ums.schedule.application.sendrequest.target.assembler.SendTargetAssembler;
import com.ums.schedule.application.sendrequest.target.data.TargetMessageData;
import com.ums.schedule.application.sendrequest.data.SendRequestKeyData;
import com.ums.schedule.application.sendrequest.target.event.SendTargetFailedEvent;
import com.ums.schedule.application.sendrequest.target.result.SendTargetSaveResult;
import com.ums.schedule.common.code.common.ChannelType;
import com.ums.schedule.common.exception.BusinessException;
import com.ums.schedule.domain.target.SendTarget;
import com.ums.schedule.domain.target.upload.TargetUploadReport;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class SendTargetUploadService {
    private final Map<ChannelType, SendTargetAssembler> targetAssemblerMap;
    private final ApplicationEventPublisher failTargetUploadPublisher;
    private final SendTargetService targetService;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public List<SendTargetSaveResult> upload(TargetUploadReport report, SendRequestKeyData keyData, List<TargetMessageData> targetList, int partitionSize) {
        SendTargetAssembler assembler = targetAssemblerMap.get(keyData.channelType());

        List<SendTargetSaveResult> results = new ArrayList<>();

        try {
            List<SendTarget> targetAssembleList = assembler.assemble(keyData.messageId(), report, targetList);
            Map<Integer, List<SendTarget>> groupedTarget = report.startTargetUploadAndGroupedTarget(targetAssembleList, partitionSize);

            Set<Map.Entry<Integer, List<SendTarget>>> entries = groupedTarget.entrySet();

            for (Map.Entry<Integer, List<SendTarget>> entry : entries) {
                SendTargetSaveResult saveResult = null;
                try {
                    saveResult = targetService.saveTargetList(entry.getValue());
                } catch (DataIntegrityViolationException e) {
                    saveResult = targetService.saveTarget(entry.getValue());
                } finally {
                    results.add(saveResult);
                }
            }
            report.completeTargetUpload(results);
        } catch (BusinessException e) {
            report.onError(e.getMessage());
        } finally {
            SendTargetFailedEvent event = SendTargetFailedEvent.of(results);
            if(!event.failureTargetList().isEmpty()) {
                failTargetUploadPublisher.publishEvent(event);
            }
        }
        return results;
    }
}