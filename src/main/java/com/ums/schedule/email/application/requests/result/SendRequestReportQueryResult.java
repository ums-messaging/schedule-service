package com.ums.schedule.email.application.requests.result;

import com.ums.schedule.email.domain.requests.EmailSendRequest;

public record SendRequestReportQueryResult(
        EmailSendRequest sendRequest,
        Long failCount
) {
}
