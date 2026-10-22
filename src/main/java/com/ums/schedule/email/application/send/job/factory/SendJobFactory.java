package com.ums.schedule.email.application.send.job.factory;

import com.ums.schedule.email.adaptor.in.config.SendJobConfigProvider;
import com.ums.schedule.email.application.requests.result.SendRequestQueryResult;
import com.ums.schedule.email.application.send.job.command.JobCommand;
import com.ums.schedule.email.application.send.job.config.SendJobConfig;
import com.ums.schedule.email.application.send.job.model.*;
import com.ums.schedule.email.application.message.factory.MessageSendDataFactory;
import com.ums.schedule.email.application.message.security.model.MessageFactoryContext;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SendJobFactory {
    private final SendJobConfigProvider provider;
    private final MessageSendDataFactory messageFactory;

    public SendJob create(JobCommand command) {
        SendRequestQueryResult result = command.result();

        MessageFactoryContext context = MessageFactoryContext.of(result);
        MessageSendData sendData = messageFactory.create(context);
        SendJobConfig config = provider.loadSendJobConfig();

        return SendJob.of(result.sendRequest(), sendData, config);
    }
}
