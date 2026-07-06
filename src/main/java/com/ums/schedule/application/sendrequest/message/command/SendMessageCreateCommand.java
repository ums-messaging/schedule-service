package com.ums.schedule.application.sendrequest.message.command;

import com.ums.schedule.common.code.mapper.EnumMapperValue;

public record SendMessageCreateCommand(
        EnumMapperValue messageType,
        String prefix,
        String title,
        String content
) {
}
