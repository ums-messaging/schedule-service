package com.ums.schedule.email.adaptor.out.persistence.jpa.repository.query;

import com.ums.schedule.email.adaptor.out.persistence.jpa.entity.SendTargetRequestEntity;
import com.ums.schedule.email.application.target.query.model.TargetDuplicatedQuery;
import com.ums.schedule.email.application.target.query.model.SendTargetGroupQuery;
import com.ums.schedule.email.domain.target.SendTargetRequest;

import java.util.List;

public interface SendTargetRequestQueryRepository {
    List<SendTargetRequest> findByTargetKeysAndContacts(TargetDuplicatedQuery query);
    List<SendTargetRequestEntity> findListBySendRequestIdAndGroupId(SendTargetGroupQuery query);
}
