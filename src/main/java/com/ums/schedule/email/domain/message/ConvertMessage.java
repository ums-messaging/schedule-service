package com.ums.schedule.email.domain.message;

import com.ums.schedule.common.code.email.ConvertType;
import com.ums.schedule.common.code.email.EmailType;
import com.ums.schedule.email.adaptor.in.rest.message.request.MessageCreateRequest;
import com.ums.schedule.email.domain.common.MultiPartFile;
import org.springframework.util.StringUtils;

public record ConvertMessage (
        ConvertType convertType,
        MultiPartFile metadata
) {

    public static ConvertMessage of(EmailType emailType, ConvertType convertType, MessageCreateRequest request) {
        ConvertType resolvedConvertType = convertType;
        if(emailType == EmailType.SECURITY && convertType == ConvertType.NONE) {
            resolvedConvertType = ConvertType.HTML;
        }
        MultiPartFile metadata = new MultiPartFile(request.fileKeyTemplate(), request.attachmentName(), request.downloadName());
        ConvertMessage convertMessage = new ConvertMessage(convertType, metadata);
        if(convertMessage.convertType != ConvertType.NONE) {
            metadata.validateAttachmentName();
            metadata.validateDownloadName();
            metadata.validateFileKeyTemplate();
        }
        return convertMessage;
    }
}
