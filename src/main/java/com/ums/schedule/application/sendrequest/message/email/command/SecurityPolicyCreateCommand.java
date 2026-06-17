package com.ums.schedule.application.sendrequest.message.email.command;

import com.ums.schedule.common.code.mapper.EnumMapperValue;
import com.ums.schedule.domain.sendrequest.resource.email.code.AttachmentEnumMapper;

public record SecurityPolicyCreateCommand(
        AttachmentEnumMapper codeKey,
        EnumMapperValue codeValue
) {
    public static SecurityPolicyCreateCommand of(AttachmentEnumMapper codeKey, EnumMapperValue codeValue) {
        if(codeValue == null) {
            return new SecurityPolicyCreateCommand(codeKey, null);
        }
        return new SecurityPolicyCreateCommand(codeKey, codeValue);
    }
}
