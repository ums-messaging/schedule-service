package com.ums.schedule.application.sendrequest.message.email.command;

import com.ums.schedule.common.code.mapper.EnumMapperValue;
import com.ums.schedule.domain.sendrequest.resource.email.code.AttachmentEnumMapper;

import java.util.Map;

public record SecurityPolicyCommand(
        Map<AttachmentEnumMapper, EnumMapperValue> securityMap,
        String passwordFormat,
        String passwordHash
) {
    public static SecurityPolicyCommand of(EmailConvertPolicyCommand policy, Map<AttachmentEnumMapper, EnumMapperValue> mapperValue) {
        return new SecurityPolicyCommand(mapperValue, policy.passwordFormat(), policy.passwordFormat());
    }
}
