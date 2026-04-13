package com.ums.schedule.application.channel.email;

import com.ums.schedule.application.channel.email.assembler.EmailBodyAssembler;
import com.ums.schedule.application.request.SendRequestService;
import com.ums.schedule.adapter.api.send.email.EmailSendCreateRequest;
import com.ums.schedule.domain.channel.email.message.EmailBody;
import com.ums.schedule.domain.channel.email.EmailSendRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmailSendRequestService  {
    private final EmailBodyAssembler assembler;
    private final SendRequestService requestService;

    public EmailSendRequest create(String customerId, EmailSendCreateRequest command) {
        EmailBody emailBody = assembler.createEmailBody(command);
        EmailSendRequest request = EmailSendRequest.of(emailBody);
        requestService.create(customerId, command.sendRequest(), request);
        return request;
    }
}
