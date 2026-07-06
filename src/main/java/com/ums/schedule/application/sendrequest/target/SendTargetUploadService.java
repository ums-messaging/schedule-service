package com.ums.schedule.application.sendrequest.target;

import com.ums.schedule.application.sendrequest.target.assembler.SendTargetAssembler;
import com.ums.schedule.application.sendrequest.data.SendRequestKeyData;
import com.ums.schedule.application.sendrequest.target.data.TargetMessageData;
import com.ums.schedule.domain.sendrequest.code.ChannelTypeEnum;
import com.ums.schedule.domain.sendrequest.target.SendTarget;
import com.ums.schedule.domain.sendrequest.target.code.SendTargetStatusEnum;
import com.ums.schedule.domain.sendrequest.target.upload.TargetUploadReport;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
public class SendTargetUploadService {
    private final Map<ChannelTypeEnum, SendTargetAssembler> targetAssemblerMap;
    private final SendTargetService targetService;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void upload(TargetUploadReport report, SendRequestKeyData keyData, List<TargetMessageData> targetList) {
        SendTargetAssembler assembler = targetAssemblerMap.get(keyData.channelType());

        AtomicLong completeTargetCount = new AtomicLong();
        AtomicLong failTargetCount = new AtomicLong();

        Map<SendTargetStatusEnum, List<SendTarget>> saveTargetListResult = null;
        Map<SendTargetStatusEnum, List<SendTarget>> targetMap = assembler.assemble(keyData.messageId(), report, targetList);

        List<SendTarget> failTargetList = null;
        List<SendTarget> completeTargetList = null;

        try {
            completeTargetList = targetService.saveTargetList(targetMap.get(SendTargetStatusEnum.READY));
        } catch (DataIntegrityViolationException e) {
            saveTargetListResult = targetService.saveTarget(targetMap.get(SendTargetStatusEnum.READY));
        } finally {
            if(saveTargetListResult != null) {
                failTargetList = Stream.concat(failTargetList.stream(), saveTargetListResult.get(SendTargetStatusEnum.FAIL).stream()).toList();
                completeTargetList = saveTargetListResult.get(SendTargetStatusEnum.COMPLETED);
            } else {
                failTargetList = targetMap.get(SendTargetStatusEnum.FAIL);
            }
            failTargetCount.addAndGet(failTargetList.size());
            completeTargetCount.addAndGet(completeTargetList.size());
            report.completeTargetUpload(completeTargetCount.get(), failTargetCount.get());
        }
    }
}