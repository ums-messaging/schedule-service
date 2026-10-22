package com.ums.schedule.email.domain.requests;
import com.ums.schedule.email.application.requests.result.SendRequestQueryResult;
import com.ums.schedule.email.domain.requests.result.SendRequestReport;

import java.util.Optional;

public interface EmailSendRequestRepository {
    EmailSendRequest save(EmailSendRequest sendRequest);
    Optional<EmailSendRequest> findById(Long id);
    Optional<SendRequestQueryResult> findByIdForSendRequest(Long id);
    void updateStatus(EmailSendRequest sendRequest);
    EmailSendRequest updateRequestReport(Long requestId, SendRequestReport sendReport);
}
