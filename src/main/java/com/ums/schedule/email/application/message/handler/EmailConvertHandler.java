package com.ums.schedule.email.application.message.handler;

import com.ums.schedule.common.code.email.security.EncryptionTypeEnum;
import com.ums.schedule.common.code.email.ConvertType;
import com.ums.schedule.email.application.send.mime.model.MimeAttachFile;

import java.nio.file.Path;
import java.util.Map;

public interface EmailConvertHandler {
    boolean supports(ConvertType convertType, EncryptionTypeEnum encryptionType);
    Path handle(MimeAttachFile attachFile, Map<String, Object> dataParam) throws Exception;
}


