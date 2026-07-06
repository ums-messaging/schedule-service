package com.ums.schedule.application.sendrequest;

import com.ums.schedule.adapter.api.request.request.EmailSendCreateRequest;
import com.ums.schedule.adapter.api.request.response.EmailSendRequestCreateResponse;
import com.ums.schedule.application.sendrequest.SendRequestService;
import com.ums.schedule.application.sendrequest.message.email.EmailMessageCreateService;
import com.ums.schedule.application.sendrequest.target.report.TargetUploadReportFactory;
import com.ums.schedule.application.sendrequest.target.result.TargetUploadResult;
import com.ums.schedule.domain.sendrequest.message.email.EmailSendMessage;
import com.ums.schedule.domain.sendrequest.SendRequest;
import com.ums.schedule.domain.sendrequest.code.ChannelTypeEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class EmailSendRequestCreateService {
    private final EmailMessageCreateService messageService;
    private final SendRequestService sendRequestService;
    private final TargetUploadReportFactory targetUploadService;

    @Transactional
    public EmailSendRequestCreateResponse create(String customerId, EmailSendCreateRequest emailCreateRequest) {
        SendRequest sendRequest = sendRequestService.create(customerId, ChannelTypeEnum.EMAIL, emailCreateRequest.request());
        EmailSendMessage message = messageService.create(sendRequest, emailCreateRequest);
        TargetUploadResult uploadResult = targetUploadService.create(emailCreateRequest.request(), sendRequest, message);

        return EmailSendRequestCreateResponse.of(sendRequest, message, uploadResult);
    }
}