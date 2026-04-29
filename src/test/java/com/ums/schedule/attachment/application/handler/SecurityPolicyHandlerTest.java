package com.ums.schedule.attachment.application.handler;

import com.ums.schedule.application.channel.email.converter.handler.SecurityPolicyHandler;
import com.ums.schedule.code.EnumMapperFactory;
import com.ums.schedule.code.EnumMapperRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith(MockitoExtension.class)
class SecurityPolicyHandlerTest {
    private SecurityPolicyHandler handler;

    private EnumMapperFactory getEnumMapperFactory() {
        EnumMapperFactory factory = new EnumMapperFactory();
        new EnumMapperRegistry(factory);
        return factory;
    }

    @Test
    @DisplayName("SecurityPolicy가 NULL이 아니면 Attachment의 ConvertType은 NONE이 아니다.")
    void shouldReturnAttachmentConvertTypeIsNotNone_whenSecurityPolicyIsNotNull() {

    }

    @Test
    @DisplayName("ConvertType이 NULL이고 SecurityPolicy가 NULL이 아니면 Attachment의 ConvertType은 HTML이다.")
    void shouldReturnAttachmentConvertTypeIsHtml_whenConvertTypeIsNullAndAttachmentConvertTypeIsNull() {

    }

    @Test
    @DisplayName("SecurityPolicyCommand가 NULL이 아니면 Attachment의 SecurityPolicy는 NULL이 아니다.")
    void shouldReturnAttachmentSecurityPolicyIsNotNull_whenSecurityPolicyCommandIsNotNull() {

    }

    @Test
    @DisplayName("SecurityPolicyCommand가 NULL이고, ConvertType이 NULL이면 Attachment의 ConvertType은 NONE이다.")
    void shouldReturnAttachmentConvertTypeIsNone_whenSecurityPolicyCommandAndConvertTypeIsNull() {

    }

    @Test
    @DisplayName("SecurityPolicyCommand가 NULL이고, ConvertType이 NULL이 아니면 Attachment의 ConvertType은 NONE이 아니다.")
    void shouldReturnAttachmentConvertTypeIsNone_whenSecurityPolicyCommandIsNullAndConvertTypeIsNotNull() {

    }
}