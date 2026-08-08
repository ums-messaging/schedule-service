package com.ums.schedule.application.ums.email.generator.handler;

import com.ums.schedule.application.ums.email.generator.handler.model.EmailConvertContext;
import com.ums.schedule.common.code.email.ConvertType;
import com.ums.schedule.common.code.email.EmailType;

import java.io.File;
import java.io.IOException;

public interface EmailConvertHandler {
    boolean supports(ConvertType convertType, EmailType emailType);
    File handle(EmailConvertContext context) throws IOException ;
}


