package com.ums.schedule.application.channel.email;

import com.ums.schedule.application.channel.email.creator.EmailBodyCreator;
import com.ums.schedule.application.target.upload.TargetUploadService;
import com.ums.schedule.adapter.api.send.email.EmailSendCreateRequest;
import com.ums.schedule.code.send.ChannelTypeEnum;
import com.ums.schedule.domain.channel.email.EmailSendRequestJpaRepository;
import com.ums.schedule.domain.channel.email.message.EmailBody;
import com.ums.schedule.domain.channel.email.EmailSendRequest;
import com.ums.schedule.domain.target.upload.TargetUpload;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class EmailSendRequestService  {
    private final EmailBodyCreator assembler;
    private final TargetUploadService targetUploadService;
    private final EmailSendRequestJpaRepository repository;

    @Transactional
    public EmailSendRequest create(String customerId, EmailSendCreateRequest command) {
        EmailBody emailBody = assembler.createEmailBody(command);
        TargetUpload targetUpload = targetUploadService.create(customerId, ChannelTypeEnum.EMAIL, command.sendRequest());
        EmailSendRequest request = EmailSendRequest.of(emailBody, targetUpload);
        return repository.save(request);
    }
}
