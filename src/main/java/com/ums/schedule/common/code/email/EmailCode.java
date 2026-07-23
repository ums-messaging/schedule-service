package com.ums.schedule.common.code.email;

import com.ums.schedule.common.code.mapper.EnumMapper;
import com.ums.schedule.common.code.mapper.EnumMapperType;

public enum EmailCode implements EnumMapper {
    ATTACHMENT_TYPE(AttachmentType.class),
    CONVERT_TYPE(ConvertType.class),
    DNS_QUERY_RESULT(DnsQueryResult.class),
    CACHE_KEY(EmailCacheKey.class),
    ENCODING_TYPE(EmailEncodingType.class),
    MESSAGE_SECTION(EmailMessageSection.class),
    TEMPLATE_FORMAT(EmailTemplateFormat.class),
    RESULT_CODE(EmailResultCode.class),
    UPLOAD_PREFIX(EmailUploadPrefixType.class),
    SMTP_COMMAND(SmtpCommandType.class)
    ,;

    Class<? extends EnumMapperType> code;

    EmailCode(Class<? extends EnumMapperType> code) {
        this.code = code;
    }


    @Override
    public String key() {
        return this.name();
    }

    @Override
    public Class<? extends EnumMapperType> code() {
        return this.code;
    }
}
