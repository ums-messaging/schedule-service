package com.ums.schedule.send.domain.target;

import com.ums.schedule.send.application.model.dto.SendTargetDto;

public record DomainGroupTarget (
        String domain,
        SendTargetDto sendTarget
) {
}
