package com.ums.schedule.send.application.service;

import com.ums.schedule.message.application.command.EmailMessageCommand;
import com.ums.schedule.send.application.assembler.EmailBodyAssembler;
import com.ums.schedule.send.application.assembler.TargetDbUploadService;
import com.ums.schedule.send.application.factory.EmailFactory;
import com.ums.schedule.send.application.factory.SendRequestFactory;
import com.ums.schedule.send.application.model.dto.SendRequestDto;
import com.ums.schedule.send.application.model.dto.TargetUploadDto;
import com.ums.schedule.send.domain.request.EmailBody;
import com.ums.schedule.send.domain.request.EmailSendRequest;
import com.ums.schedule.send.domain.request.SendRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmailSendRequestService  {
    private final EmailBodyAssembler assembler;
    private final SendRequestService requestService;

    public EmailSendRequest create(String customerId, EmailMessageCommand command) {
        EmailBody emailBody = assembler.createEmailBody(command);
        EmailSendRequest request = EmailSendRequest.of(emailBody);
        requestService.create(customerId, command.sendRequest(), request);
        return request;
    }
}
