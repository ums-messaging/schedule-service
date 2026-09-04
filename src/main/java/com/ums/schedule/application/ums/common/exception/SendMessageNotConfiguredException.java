package com.ums.schedule.application.ums.common.exception;

import com.ums.schedule.common.code.api.SendMessageErrorCode;
import com.ums.schedule.common.code.message.MessageConfigurationPrefix;
import com.ums.schedule.common.exception.NotConfiguredException;

public class SendMessageNotConfiguredException extends NotConfiguredException {
    protected SendMessageNotConfiguredException(MessageConfigurationPrefix prefix) {
        super(prefix.description());
    }

    public static SendMessageNotConfiguredException of(MessageConfigurationPrefix prefix) {
        return new SendMessageNotConfiguredException(prefix);
    }
}
