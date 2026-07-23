package com.ums.schedule.application.ums.email.request;

import com.ums.schedule.adapter.api.request.email.request.EmailSendCreateRequest;
import com.ums.schedule.application.ums.common.request.SendRequestCreateService;
import com.ums.schedule.application.sendrequest.command.SendRequestCreateCommand;
import com.ums.schedule.application.ums.common.request.model.SendRequestCreateResult;
import com.ums.schedule.application.ums.email.message.EmailMessageCreateService;
import com.ums.schedule.application.ums.email.request.model.EmailSendRequestCreateSummary;
import com.ums.schedule.common.code.common.ChannelType;
import com.ums.schedule.common.code.target_upload.TargetUploadType;
import com.ums.schedule.domain.message.email.EmailSendMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class EmailSendRequestCreateService {
    private final EmailMessageCreateService messageService;
    private final SendRequestCreateService sendRequestService;

    @Transactional
    public EmailSendRequestCreateSummary create(String customerId, TargetUploadType uploadType, EmailSendCreateRequest request) {
        EmailSendMessage sendMessage = messageService.create(customerId, request);
        SendRequestCreateCommand command = request.request().toCommand(customerId, request.senderKey(), ChannelType.EMAIL, uploadType);
        SendRequestCreateResult result = sendRequestService.create(command, sendMessage.getSendMessage());

        return EmailSendRequestCreateSummary.of(sendMessage, result);
    }
}