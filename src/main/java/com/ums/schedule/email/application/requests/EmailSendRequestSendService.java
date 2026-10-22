package com.ums.schedule.email.application.requests;

import com.ums.schedule.email.application.requests.result.SendRequestQueryResult;
import com.ums.schedule.email.common.code.EmailSendStatusCode;
import com.ums.schedule.email.domain.requests.EmailSendRequest;
import com.ums.schedule.email.domain.requests.EmailSendRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EmailSendRequestSendService {
    private final EmailSendRequestRepository repository;

    @Transactional
    public SendRequestQueryResult findSendRequest(Long requestId) {
        SendRequestQueryResult result = repository.findByIdForSendRequest(requestId).orElseThrow();
        EmailSendRequest sendRequest = result.sendRequest();
        if(sendRequest.getState() != EmailSendStatusCode.CREATE) { // 중복 발송 방지
            throw new IllegalArgumentException("이미 처리됨");
        }
        sendRequest.startSend(); // SENDING으로 변경
        repository.updateStatus(sendRequest);
        return result;
    }
}
