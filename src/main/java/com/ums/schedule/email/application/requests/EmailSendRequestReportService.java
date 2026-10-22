package com.ums.schedule.email.application.requests;

import com.ums.schedule.email.adaptor.out.persistence.jpa.repository.query.EmailSendRequestQueryRepository;
import com.ums.schedule.email.application.requests.result.SendRequestReportQueryResult;
import com.ums.schedule.email.application.target.reader.model.SendTargetsReadResult;
import com.ums.schedule.email.domain.requests.EmailSendRequest;
import com.ums.schedule.email.domain.requests.EmailSendRequestRepository;
import com.ums.schedule.email.domain.requests.result.SendRequestReport;
import com.ums.schedule.email.domain.target.SendTargetErrorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EmailSendRequestReportService {
    private final SendTargetErrorRepository errorRepository;
    private final EmailSendRequestRepository repository;

    @Transactional
    public SendRequestReport updateSendRequestReport(Long requestId, SendTargetsReadResult result) {
        int failedCount = errorRepository.countRequestFailCount(requestId);
        SendRequestReport sendReport = SendRequestReport.of(result, failedCount);
        repository.updateRequestReport(requestId, sendReport);
        return sendReport;
    }
}
