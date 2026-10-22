package com.ums.schedule.email.application.requests.result;

import com.ums.schedule.email.adaptor.out.persistence.jpa.entity.EmailSendRequestEntity;

public record SendRequestReportQueryResult(
        EmailSendRequestEntity sendRequest,
        Long failCount
) {
}
