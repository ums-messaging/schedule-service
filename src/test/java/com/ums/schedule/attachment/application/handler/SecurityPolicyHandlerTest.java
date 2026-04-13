package com.ums.schedule.attachment.application.handler;

import com.ums.schedule.application.channel.email.converter.handler.SecurityPolicyHandler;
import com.ums.schedule.domain.channel.email.attachment.Attachment;
import com.ums.schedule.domain.channel.email.security.SecurityPolicy;
import com.ums.schedule.attachment.fixture.AttachmentFixture;
import com.ums.schedule.attachment.fixture.builder.EmailMessageCommandBuilder;
import com.ums.schedule.code.EnumMapperFactory;
import com.ums.schedule.code.EnumMapperRegistry;
import com.ums.schedule.adapter.api.send.email.EmailSendCreateRequest;
import com.ums.schedule.adapter.api.template.email.EmailContentResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.ums.schedule.attachment.fixture.AttachmentFixture.ofSecurityPolicy;
import static com.ums.schedule.domain.template.domain.code.ConvertTypeEnum.NONE;
import static com.ums.schedule.domain.template.domain.code.ConvertTypeEnum.PDF;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class SecurityPolicyHandlerTest {
    private SecurityPolicyHandler handler;

    @BeforeEach
    void setUp() {
        this.handler = new SecurityPolicyHandler(getEnumMapperFactory());
    }

    private EnumMapperFactory getEnumMapperFactory() {
        EnumMapperFactory factory = new EnumMapperFactory();
        new EnumMapperRegistry(factory);
        return factory;
    }

    @Test
    @DisplayName("SecurityPolicy가 NULL이 아니면 Attachment의 ConvertType은 NONE이 아니다.")
    void shouldReturnAttachmentConvertTypeIsNotNone_whenSecurityPolicyIsNotNull() {
        EmailSendCreateRequest command = ofSecurityPolicy();
        EmailContentResponse body = AttachmentFixture.bodyOfHtml();

        Attachment result = handler.handle(command, body);
        ConvertTypeEnum expect = result.getConvertType();

        assertThat(expect).isNotEqualTo(ConvertTypeEnum.NONE);
    }

    @Test
    @DisplayName("ConvertType이 NULL이고 SecurityPolicy가 NULL이 아니면 Attachment의 ConvertType은 HTML이다.")
    void shouldReturnAttachmentConvertTypeIsHtml_whenConvertTypeIsNullAndAttachmentConvertTypeIsNull() {
        EmailSendCreateRequest command = AttachmentFixture.ofSecurityPolicyAndConvertTypeIsNull();
        EmailContentResponse body = AttachmentFixture.bodyOfHtml();

        Attachment result = handler.handle(command, body);
        ConvertTypeEnum expect = result.getConvertType();

        assertThat(expect).isEqualTo(ConvertTypeEnum.HTML);
    }

    @Test
    @DisplayName("SecurityPolicyCommand가 NULL이 아니면 Attachment의 SecurityPolicy는 NULL이 아니다.")
    void shouldReturnAttachmentSecurityPolicyIsNotNull_whenSecurityPolicyCommandIsNotNull() {
        EmailSendCreateRequest command = ofSecurityPolicy();
        EmailContentResponse body = AttachmentFixture.bodyOfHtml();

        Attachment result = handler.handle(command, body);
        SecurityPolicy expected = result.getSecurityPolicy();

        assertThat(expected).isNotNull();
    }

    @Test
    @DisplayName("SecurityPolicyCommand가 NULL이고, ConvertType이 NULL이면 Attachment의 ConvertType은 NONE이다.")
    void shouldReturnAttachmentConvertTypeIsNone_whenSecurityPolicyCommandAndConvertTypeIsNull() {
        EmailSendCreateRequest command = EmailMessageCommandBuilder.builder().securityPolicy(null).convertType(null).build();
        EmailContentResponse body = AttachmentFixture.bodyOfHtml();

        Attachment result = handler.handle(command, body);
        ConvertTypeEnum expected = result.getConvertType();

        assertThat(expected).isEqualTo(ConvertTypeEnum.NONE);
    }

    @Test
    @DisplayName("SecurityPolicyCommand가 NULL이고, ConvertType이 NULL이 아니면 Attachment의 ConvertType은 NONE이 아니다.")
    void shouldReturnAttachmentConvertTypeIsNone_whenSecurityPolicyCommandIsNullAndConvertTypeIsNotNull() {
        EmailSendCreateRequest command = EmailMessageCommandBuilder.builder()
                .securityPolicy(ofSecurityPolicy().securityPolicy()).convertType(PDF.value()).build();
        EmailContentResponse body = AttachmentFixture.bodyOfHtml();

        Attachment result = handler.handle(command, body);
        ConvertTypeEnum expected = result.getConvertType();

        assertThat(expected).isNotEqualTo(NONE);
        assertThat(expected).isEqualTo(PDF);
    }
}