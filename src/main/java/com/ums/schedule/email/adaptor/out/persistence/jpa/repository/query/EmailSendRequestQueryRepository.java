package com.ums.schedule.email.adaptor.out.persistence.jpa.repository.query;

import com.ums.schedule.email.application.requests.result.SendRequestReportQueryResult;

public interface EmailSendRequestQueryRepository {
    SendRequestReportQueryResult countSendRequestReport(Long requestId);
}
