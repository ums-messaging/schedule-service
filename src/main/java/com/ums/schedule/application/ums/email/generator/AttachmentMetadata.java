package com.ums.schedule.application.ums.email.generator;

import com.ums.schedule.common.code.email.ConvertType;


public record AttachmentMetadata(
        ConvertType convertType,
        String fileKeyTemplate,
        String attachmentName,
        String downloadName
) {

}
