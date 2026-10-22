package com.ums.schedule.email.adaptor.out.queue.kafka;

import com.github.f4b6a3.tsid.TsidCreator;
import com.ums.schedule.common.code.request.SendGroupEventType;
import com.ums.schedule.common.util.JsonUtil;
import com.ums.schedule.email.application.event.SendGroupEventDispatcherFactory;
import com.ums.schedule.email.application.event.SendGroupEventKafkaFactory;
import com.ums.schedule.email.application.event.SendGroupRequestEventService;
import com.ums.schedule.email.application.target.SendTargetErrorService;
import com.ums.schedule.email.domain.event.SendGroupEventResult;
import com.ums.schedule.email.domain.event.SendTargetGroupEvent;
import com.ums.schedule.email.domain.event.SendTargetGroupEventRepository;
import com.ums.schedule.email.domain.event.SendTargetResultList;
import com.ums.schedule.email.domain.target.SendTargetError;
import com.ums.schedule.email.domain.target.SendTargetErrorRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Component
public class SendGroupEventKafkaDispatcher {
    private final SendGroupEventDispatcherFactory factory;
    private final SendTargetErrorService errorService;
    private final SendGroupRequestEventService eventService;
    private final KafkaTemplate<String, Object> template;

    public SendGroupEventKafkaDispatcher(SendGroupEventDispatcherFactory factory,
                                         SendTargetErrorService errorService,
                                         SendGroupRequestEventService eventService,
                                         @Qualifier("kafkaTemplate") KafkaTemplate<String, Object> template) {
        this.factory = factory;
        this.errorService = errorService;
        this.eventService = eventService;
        this.template = template;
    }

    public void publish(SendGroupEventType eventType,
                        SendGroupEventResult result, SendTargetResultList resultList) {
        String eventId = TsidCreator.getTsid().toString();
        SendGroupEventKafkaFactory findFactory
                = factory.findGroupEventDispatcher(result.resultCode());

        SendTargetGroupEvent event = findFactory.createGroupEvent(eventType, result, resultList);
        List<SendTargetError> errorList = event.getErrorList();

        if(!errorList.isEmpty()) {
            log.info("error.list.save = {}", errorList.size());
            errorService.saveAll(errorList);
        }
        eventService.save(event);
//            template.send("send-target-group-event", eventId, JsonUtil.toJson(event));
    }
}
