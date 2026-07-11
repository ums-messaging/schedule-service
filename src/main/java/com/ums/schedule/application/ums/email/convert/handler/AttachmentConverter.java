package com.ums.schedule.application.ums.email.convert.handler;

import com.ums.schedule.application.message.email.model.AttachmentPipelineCommand;
import com.ums.schedule.application.message.email.result.TemplateConversionResult;
import com.ums.schedule.domain.message.email.code.ConvertTypeEnum;

public interface AttachmentConverter {
    boolean supports(ConvertTypeEnum convertType, boolean isSecurity);
    TemplateConversionResult handle(AttachmentPipelineCommand command);
}
