package com.ums.schedule.application.ums.email.convert.handler;

import com.ums.schedule.application.message.email.model.AttachmentPipelineCommand;
import com.ums.schedule.application.message.email.result.TemplateConversionResult;
import com.ums.schedule.common.code.email.ConvertType;

public interface AttachmentConverter {
    boolean supports(ConvertType convertType, boolean isSecurity);
    TemplateConversionResult handle(AttachmentPipelineCommand command);
}
