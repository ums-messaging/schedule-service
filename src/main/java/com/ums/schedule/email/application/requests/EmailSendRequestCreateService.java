package com.ums.schedule.email.application.requests;

import com.ums.schedule.email.adaptor.in.rest.send.request.EmailSendRequestRequest;
import com.ums.schedule.email.application.requests.command.SendRequestCommand;
import com.ums.schedule.email.application.requests.result.SendRequestSaveResult;
import com.ums.schedule.email.domain.requests.EmailSendRequest;
import com.ums.schedule.email.domain.requests.EmailSendRequestRepository;
import com.ums.schedule.email.domain.requests.sender.Sender;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EmailSendRequestCreateService {
    private final EmailSendRequestRepository repository;

    @Transactional
    public SendRequestSaveResult request(EmailSendRequestRequest request) {
        SendRequestCommand command = SendRequestCommand.of(request, new Sender("admin", "admin@test.com"));
        EmailSendRequest sendRequest = EmailSendRequest.of(command);
        EmailSendRequest saved = repository.save(sendRequest);
        return SendRequestSaveResult.of(saved);
    }
}
