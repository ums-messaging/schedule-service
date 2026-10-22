package com.ums.schedule.email.domain.target;

import java.util.List;

public interface SendTargetErrorRepository {
    void saveAll(List<SendTargetError> errorList);
    int countRequestFailCount(Long sendRequestId);
}
