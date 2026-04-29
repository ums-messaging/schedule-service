package com.ums.schedule.adapter.api.send.email;

import com.ums.schedule.adapter.api.send.SendCreateRequest;
import com.ums.schedule.application.target.dto.SendTargetDto;

import java.util.List;

public record EmailSendCreateRequest(
        SendCreateRequest sendRequest,
        String convertType,
        String encodingType,
        String attachmentNameFormat,
        String downloadNameFormat,
        EmailSecurityPolicyRequest securityPolicy
) {
}
