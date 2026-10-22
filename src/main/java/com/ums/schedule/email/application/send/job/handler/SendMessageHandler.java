package com.ums.schedule.email.application.send.job.handler;

import com.ums.schedule.common.code.request.SendGroupEventType;
import com.ums.schedule.email.adaptor.out.queue.kafka.SendGroupEventKafkaDispatcher;
import com.ums.schedule.email.application.send.job.model.MessageSendData;
import com.ums.schedule.email.application.send.job.model.SendJob;
import com.ums.schedule.email.application.send.loader.EmailTemplateLoader;
import com.ums.schedule.email.application.send.loader.model.TemplateLoadResult;
import com.ums.schedule.email.application.send.mime.MessageContext;
import com.ums.schedule.email.application.send.mime.model.MimeMessage;
import com.ums.schedule.email.application.target.query.model.SendTargetGroupQuery;
import com.ums.schedule.email.domain.event.SendGroupEventResult;
import com.ums.schedule.email.domain.target.SendTargetRequest;
import com.ums.schedule.email.domain.target.SendTargetRequestRepository;
import com.ums.schedule.email.domain.target.group.DomainGroup;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class SendMessageHandler {
    private final SendTargetRequestRepository repository;
    private final EmailTemplateLoader templateLoader;
    private final SendGroupEventKafkaDispatcher dispatcher;

    public MessageContext executeHandler(Path templateDir, SendJob job, DomainGroup group)  {
        Map<Integer, List<SendTargetRequest>> targetGroupList = new HashMap<>();
        MimeMessage mimeMessage = null;
        MessageContext context = null;
        try {
            SendTargetGroupQuery query = SendTargetGroupQuery.of(job, group);
            MessageSendData sendData = job.messageData();
            targetGroupList = repository.findListAndGrouping(query);
            TemplateLoadResult template = templateLoader.load(templateDir, sendData);
            mimeMessage = MimeMessage.of(job, group.groupKey(), template);
            context = MessageContext.of(mimeMessage, targetGroupList);
            dispatcher.publish(SendGroupEventType.SEND_STARTED, SendGroupEventResult.of(), context.targetResultList(job.sendRequestId(), group));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return context;
    }


}
