package com.ums.schedule.template.application.assembler;

import com.ums.schedule.code.EnumMapperFactory;
import com.ums.schedule.application.channel.email.template.EmailTemplateService;
import com.ums.schedule.application.channel.email.template.loader.TemplateFileLoader;
import com.ums.schedule.application.channel.email.template.loader.EmailTemplateLoader;
import com.ums.schedule.application.channel.email.template.loader.TemplateTextLoader;
import com.ums.schedule.adapter.api.template.email.EmailContentResponse;
import com.ums.schedule.adapter.api.template.email.EmailTemplateDetailResponse;
import com.ums.schedule.domain.channel.email.message.EmailTemplate;
import com.ums.schedule.domain.channel.email.exception.TemplateContentRequiredException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;
import java.util.stream.Stream;

import static com.ums.schedule.code.EnumMapperValue.fromEnumMapperType;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TemplateAssemblerTest {
//    @Mock private EnumMapperFactory factory;
//    @Mock private EmailTemplateLoader resolver;
//    @Mock private TemplateTextLoader textResolver;
//    @Mock private TemplateFileLoader fileResolver;
//    private EmailTemplateService assembler;
//
//    @BeforeEach
//    void setUp() {
//        assembler = new EmailTemplateService(
//                Map.of("TEXT", textResolver, "HTML", fileResolver),
//                factory
//        );
//    }
//
//    @Test
//    @DisplayName("HEADER를 입력하면 EmailTemplate의 header는 NULL이 아니다.")
//    void shouldReturnHeaderIsNotNull_whenHeaderExists() {
//        EmailContentResponse header = givenContent(HEADER);
//        EmailContentResponse body = givenContent(BODY);
//        EmailTemplateDetailResponse detail = givenEmailDetailResponse(header, body, null);
//
//        when(factory.findEnumMapperValue(any(), any())).thenReturn(fromEnumMapperType(TEXT));
//        when(textResolver.loadTemplate(any())).thenReturn(of(fromEnumMapperType(TEXT), header.content()));
//        when(textResolver.loadTemplate(any())).thenReturn(of(fromEnumMapperType(TEXT), body.content()));
//
//        EmailTemplate result = assembler.assemble(detail, body);
//
//        assertThat(result.getHeader()).isNotNull();
//    }
//
//
//    @Test
//    @DisplayName("FOOTER를 입력하면 EmailTemplate의 footer는 NULL이 아니다.")
//    void shouldReturnFooterIsNotNull_whenFooterExists() {
//        EmailContentResponse footer = givenContent(FOOTER);
//        EmailContentResponse body = givenContent(BODY);
//        EmailTemplateDetailResponse detail = givenEmailDetailResponse(null, body, footer);
//
//        when(factory.findEnumMapperValue(any(), any())).thenReturn(fromEnumMapperType(TEXT));
//        when(textResolver.loadTemplate(any())).thenReturn(of(fromEnumMapperType(TEXT),footer.content()));
//        when(textResolver.loadTemplate(any())).thenReturn(of(fromEnumMapperType(TEXT),body.content()));
//
//        EmailTemplate result = assembler.assemble(detail, body);
//
//        assertThat(result.getFooter()).isNotNull();
//    }
//
//    @Test
//    @DisplayName("BODY가 NULL이면 예외가 발생한다.")
//    void shouldThrowException_whenBodyIsNull() {
//        EmailTemplateDetailResponse detail = givenEmailDetailResponse(null, null, null);
//
//        assertThatThrownBy(() -> assembler.assemble(detail, null))
//                .isInstanceOf(TemplateContentRequiredException.class)
//                .hasMessage(TemplateContentRequiredException.ofBody().getMessage());
//
//    }
//
//    @Test
//    @DisplayName("imageDir가 존재하지 않으면 예외가 발생한다.")
//    void shouldThrowException_whenImageDirIsNull() {
//        EmailContentResponse body = givenContent(BODY);
//        EmailTemplateDetailResponse detail = new EmailTemplateDetailResponse(
//                UUID.randomUUID().toString(),
//                "이메일",
//                null,
//                List.of(body)
//        );
//
//        when(factory.findEnumMapperValue(any(), any())).thenReturn(fromEnumMapperType(TEXT));
//        when(textResolver.loadTemplate(any())).thenReturn(of(fromEnumMapperType(TEXT),body.content()));
//
//        assertThatThrownBy(() -> assembler.assemble(detail, body))
//                .isInstanceOf(TemplateContentRequiredException.class)
//                .hasMessage(TemplateContentRequiredException.ofImageDir().getMessage());
//    }
//
//    private EmailTemplateDetailResponse givenEmailDetailResponse(EmailContentResponse header, EmailContentResponse body, EmailContentResponse footer) {
//        Stream<EmailContentResponse> headerFooter =
//                Stream.concat(toStream(header), toStream(footer));
//        List<EmailContentResponse> contents = Stream.concat(headerFooter, toStream(body))
//                .toList();
//        return new EmailTemplateDetailResponse(
//                UUID.randomUUID().toString(),
//                "이메일",
//                "/template/image",
//                contents
//        );
//    }
//
//    private Stream<EmailContentResponse> toStream(EmailContentResponse content) {
//        return Optional.ofNullable(content)
//                .map(Stream::of)
//                .orElseGet(Stream::empty);
//    }
//
//    private EmailContentResponse givenContent(EmailTemplateSectionEnum section) {
//        return EmailContentResponseBuilder.builder()
//                .section(section)
//                .format(TEXT)
//                .content("<html> ".concat(section.value()).concat(" </html>"))
//                .build();
//    }
}