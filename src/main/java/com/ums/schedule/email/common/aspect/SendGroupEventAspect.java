package com.ums.schedule.email.common.aspect;

import com.github.f4b6a3.tsid.TsidCreator;
import com.github.f4b6a3.uuid.UuidCreator;
import com.ums.schedule.common.code.email.EmailResultCode;
import com.ums.schedule.common.code.request.SendGroupEventType;
import com.ums.schedule.common.exception.BusinessException;
import com.ums.schedule.common.util.JsonUtil;
import com.ums.schedule.email.application.event.SendGroupEventDispatcherFactory;
import com.ums.schedule.email.application.event.SendTargetRequestEvent;
import com.ums.schedule.email.common.aspect.annotation.SendGroupEventExecution;
import com.ums.schedule.email.domain.event.DomainGroupEvent;
import com.ums.schedule.email.domain.event.SendGroupEventResult;
import com.ums.schedule.email.domain.event.SendTargetGroupEvent;
import com.ums.schedule.email.domain.event.SendTargetResultList;
import com.ums.schedule.email.domain.target.SendTargetError;
import com.ums.schedule.email.domain.target.SendTargetErrorRepository;
import com.ums.schedule.email.domain.target.group.DomainGroup;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class SendGroupEventAspect {
    private final SendGroupEventDispatcherFactory factory;

//    @Around("@annotation(sendGroupEventExecution)")
//    public Object invoke(ProceedingJoinPoint jointPoint,
//                         SendGroupEventExecution sendGroupEventExecution) throws Throwable {
//        try {
//            SendGroupEventType type = sendGroupEventExecution.value();
//            try {
//                SendTargetResultList resultList = (SendTargetResultList) jointPoint.proceed();
//                DomainGroupEvent groupEvent = createDomainGroupEvent(type, resultList);
//                SendTargetGroupEvent event = SendTargetGroupEvent.of(groupEvent, SendGroupEventResult.of(), resultList);
//            } catch (Exception e) {
//                SendTargetGroupEvent event = SendTargetGroupEvent.of(groupEvent, SendGroupEventResult.of(), resultList);
//            }
//            return resultList;
//        } catch (Exception e) {
//            log.error("");
//        }
//    }

    private DomainGroupEvent createDomainGroupEvent(SendGroupEventType type, SendTargetResultList resultList) {
        Long requestId = resultList.sendRequestId();
        DomainGroup domainGroup = resultList.domainGroup();
        return DomainGroupEvent.of(requestId, type, domainGroup);
    }
}
