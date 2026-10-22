package com.ums.schedule.email.adaptor.out.persistence.jpa.repository.query;

import com.ums.schedule.email.adaptor.out.persistence.jpa.repository.projection.SendRequestReportQueryProjection;

import java.util.Optional;

public interface EmailSendRequestQueryRepository {
    Optional<SendRequestReportQueryProjection> countSendRequestReport(Long requestId);
}
