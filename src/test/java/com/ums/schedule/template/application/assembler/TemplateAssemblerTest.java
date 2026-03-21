package com.ums.schedule.template.application.assembler;

import com.ums.schedule.attachment.exception.AttachmentPolicyRequiredException;
import com.ums.schedule.attachment.fixture.builder.EmailContentResponseBuilder;
import com.ums.schedule.attachment.infrastructure.AwsS3Repository;
import com.ums.schedule.common.code.EnumMapperFactory;
import com.ums.schedule.common.code.EnumMapperValue;
import com.ums.schedule.template.application.resolver.TemplateFileResolver;
import com.ums.schedule.template.application.resolver.TemplateFormatResolver;
import com.ums.schedule.template.application.resolver.TemplateTextResolver;
import com.ums.schedule.template.application.response.email.EmailContentResponse;
import com.ums.schedule.template.application.response.email.EmailTemplateDetailResponse;
import com.ums.schedule.template.domain.code.EmailTemplateSectionEnum;
import com.ums.schedule.template.domain.code.TemplateContentFormatEnum;
import com.ums.schedule.template.domain.email.EmailContent;
import com.ums.schedule.template.domain.email.EmailTemplate;
import com.ums.schedule.template.exception.TemplateContentRequiredException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;
import java.util.stream.Stream;

import static com.ums.schedule.common.code.EnumMapperValue.fromEnumMapperType;
import static com.ums.schedule.template.domain.code.EmailTemplateSectionEnum.*;
import static com.ums.schedule.template.domain.code.TemplateContentFormatEnum.*;
import static com.ums.schedule.template.domain.email.EmailContent.of;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TemplateAssemblerTest {
    @Mock private EnumMapperFactory factory;
    @Mock private TemplateFormatResolver resolver;
    @Mock private TemplateTextResolver textResolver;
    @Mock private TemplateFileResolver fileResolver;
    private TemplateAssembler assembler;

    @BeforeEach
    void setUp() {
        assembler = new TemplateAssembler(
                Map.of("TEXT", textResolver, "HTML", fileResolver),
                factory
        );
    }

    @Test
    @DisplayName("HEADER를 입력하면 EmailTemplate의 header는 NULL이 아니다.")
    void shouldReturnHeaderIsNotNull_whenHeaderExists() {
        EmailContentResponse header = givenContent(HEADER);
        EmailContentResponse body = givenContent(BODY);
        EmailTemplateDetailResponse detail = givenEmailDetailResponse(header, body, null);

        when(factory.findEnumMapperValue(any(), any())).thenReturn(fromEnumMapperType(TEXT));
        when(textResolver.loadTemplate(any())).thenReturn(of(header.content()));
        when(textResolver.loadTemplate(any())).thenReturn(of(body.content()));

        EmailTemplate result = assembler.assemble(detail, body);

        assertThat(result.getHeader()).isNotNull();
    }


    @Test
    @DisplayName("FOOTER를 입력하면 EmailTemplate의 footer는 NULL이 아니다.")
    void shouldReturnFooterIsNotNull_whenFooterExists() {
        EmailContentResponse footer = givenContent(FOOTER);
        EmailContentResponse body = givenContent(BODY);
        EmailTemplateDetailResponse detail = givenEmailDetailResponse(null, body, footer);

        when(factory.findEnumMapperValue(any(), any())).thenReturn(fromEnumMapperType(TEXT));
        when(textResolver.loadTemplate(any())).thenReturn(of(footer.content()));
        when(textResolver.loadTemplate(any())).thenReturn(of(body.content()));

        EmailTemplate result = assembler.assemble(detail, body);

        assertThat(result.getFooter()).isNotNull();
    }

    @Test
    @DisplayName("BODY가 NULL이면 예외가 발생한다.")
    void shouldThrowException_whenBodyIsNull() {
        EmailTemplateDetailResponse detail = givenEmailDetailResponse(null, null, null);

        assertThatThrownBy(() -> assembler.assemble(detail, null))
                .isInstanceOf(TemplateContentRequiredException.class)
                .hasMessage(TemplateContentRequiredException.ofBody().getMessage());

    }

    @Test
    @DisplayName("title이 존재하지 않으면 예외가 발생한다.")
    void shouldThrowException_wheniItleIsNull() {
        EmailContentResponse body = givenContent(BODY);
        EmailTemplateDetailResponse detail = new EmailTemplateDetailResponse(
                UUID.randomUUID().toString(),
                null,
                "template/images",
                List.of(body)
        );

        when(factory.findEnumMapperValue(any(), any())).thenReturn(fromEnumMapperType(TEXT));
        when(textResolver.loadTemplate(any())).thenReturn(of(body.content()));

        assertThatThrownBy(() -> assembler.assemble(detail, body))
                .isInstanceOf(TemplateContentRequiredException.class)
                .hasMessage(TemplateContentRequiredException.ofTitle().getMessage());
    }

    @Test
    @DisplayName("imageDir가 존재하지 않으면 예외가 발생한다.")
    void shouldThrowException_whenImageDirIsNull() {
        EmailContentResponse body = givenContent(BODY);
        EmailTemplateDetailResponse detail = new EmailTemplateDetailResponse(
                UUID.randomUUID().toString(),
                "이메일",
                null,
                List.of(body)
        );

        when(factory.findEnumMapperValue(any(), any())).thenReturn(fromEnumMapperType(TEXT));
        when(textResolver.loadTemplate(any())).thenReturn(of(body.content()));

        assertThatThrownBy(() -> assembler.assemble(detail, body))
                .isInstanceOf(TemplateContentRequiredException.class)
                .hasMessage(TemplateContentRequiredException.ofImageDir().getMessage());
    }

    private EmailTemplateDetailResponse givenEmailDetailResponse(EmailContentResponse header, EmailContentResponse body, EmailContentResponse footer) {
        Stream<EmailContentResponse> headerFooter =
                Stream.concat(toStream(header), toStream(footer));
        List<EmailContentResponse> contents = Stream.concat(headerFooter, toStream(body))
                .toList();
        return new EmailTemplateDetailResponse(
                UUID.randomUUID().toString(),
                "이메일",
                "/template/image",
                contents
        );
    }

    private Stream<EmailContentResponse> toStream(EmailContentResponse content) {
        return Optional.ofNullable(content)
                .map(Stream::of)
                .orElseGet(Stream::empty);
    }

    private EmailContentResponse givenContent(EmailTemplateSectionEnum section) {
        return EmailContentResponseBuilder.builder()
                .section(section)
                .format(TEXT)
                .content("<html> ".concat(section.value()).concat(" </html>"))
                .build();
    }
}