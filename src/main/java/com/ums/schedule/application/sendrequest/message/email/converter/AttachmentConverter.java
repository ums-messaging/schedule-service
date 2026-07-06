package com.ums.schedule.application.sendrequest.message.email.converter;

import com.ums.schedule.application.sendrequest.message.email.command.AttachmentPipelineCommand;
import com.ums.schedule.application.sendrequest.message.email.result.TemplateConversionResult;
import com.ums.schedule.domain.sendrequest.resource.email.code.ConvertTypeEnum;

public interface AttachmentConverter {
    boolean supports(ConvertTypeEnum convertType, boolean isSecurity);
    TemplateConversionResult handle(AttachmentPipelineCommand command);
}
