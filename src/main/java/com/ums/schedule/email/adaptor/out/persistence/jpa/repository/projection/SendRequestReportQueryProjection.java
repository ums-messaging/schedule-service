package com.ums.schedule.email.adaptor.out.persistence.jpa.repository.projection;

import com.ums.schedule.email.adaptor.out.persistence.jpa.entity.EmailSendRequestEntity;

public record SendRequestReportQueryProjection(
        EmailSendRequestEntity sendRequest,
        Long failCount
) {
}
