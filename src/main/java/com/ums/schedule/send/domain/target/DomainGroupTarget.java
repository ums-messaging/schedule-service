package com.ums.schedule.send.domain.target;

import com.ums.schedule.send.application.model.dto.SendTargetRowDto;

public record DomainGroupTarget (
        String domain,
        SendTargetRowDto sendTarget
) {
}
