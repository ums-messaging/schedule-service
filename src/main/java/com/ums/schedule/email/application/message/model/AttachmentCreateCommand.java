package com.ums.schedule.email.application.message.model;

import com.ums.schedule.email.adaptor.in.rest.message.request.AttachmentRequest;
import com.ums.schedule.email.application.attachment.model.AttachmentFindResult;
import com.ums.schedule.email.application.template.query.model.EmailTemplateContent;
import com.ums.schedule.common.code.mapper.EnumMapperValue;
import com.ums.schedule.common.code.email.ConvertType;
import com.ums.schedule.common.code.email.security.SecurityMailCode;

import java.util.Map;

public record AttachmentCreateCommand(
        EnumMapperValue convertType,
        String attachmentName,
        String downloadName,
        String fileKeyTemplate,
        String fileKey,
        Long fileSize,
        Map<SecurityMailCode, EnumMapperValue> securityPolicyMap,
        String passwordHash,
        String passwordFormat
) {

}
