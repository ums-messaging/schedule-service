package com.ums.schedule.message.application.processor;

import com.ums.schedule.application.channel.email.converter.handler.PdfConvertHandler;
import com.ums.schedule.code.EnumMapperFactory;
import com.ums.schedule.application.channel.email.template.EmailTemplateService;
import com.ums.schedule.adapter.api.template.email.EmailTemplateClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class EmailMessageProcessorTest {
    @Mock private EmailTemplateClient templateClient;
    @Mock private PdfConvertHandler handler;
    @Mock private EnumMapperFactory factory;
    @Mock private EmailTemplateService assembler;

    @BeforeEach
    public void setUp() {

    }

    /**
     * Given
     *  - TemplateClient 응답 값으로 TemplateType에 Advertise와 msgTitle을 준다.
     *  - TemplateTypeResolver의 반환 값으로 ADVERTISE를 준다.
     *
     * Then
     *  - Resolver가 실행되는지 검증한다.
     *  - TemplateType이 ADVERTISE가 아니면 resolver는 실행되지 않는다.
     * */
    @Test
    @DisplayName("TemplateType이 Advertise이면, TemplateTypeResolver가 실행된다. ")
    void shouldContainAdvertiseTexture_whenTemplateTypeIsAdvertise() {
//        EmailSendCreateRequest command = EmailMessageCommandBuilder.builder().build();
//        EmailTemplateDetailResponse emailTemplateResponse = givenEmailTemplateDetailResponse(getContent(BODY));
//
//        givenTemplateClient(emailTemplateResponse);
//        givenTemplateTypeResolver("(광고) ", "메시지 제목");
//        givenAttachmentHandler(null, List.of());
//        givenTemplateAssembler(emailTemplateResponse, emailTemplateResponse.getBody().content());
//
//        // When
//        processor.createMessage(command);
//
//        // Then
//        verify(resolver).appendPrefixTexture(emailTemplateResponse.msgTitle());
    }

    /**
     * Given
     *  - TemplateClient 응답 값으로 TemplateType에 Advertise와 msgTitle을 준다.
     *  - TemplateTypeResolver의 반환 값으로 ADVERTISE를 준다.
     *  - TemplateTypeResolver의 실행 결과를 준다.
     *
     * Then
     *  - SendMessage의 title 문구 앞에 '(광고)' 문구와 msgTitle이 포함되는지 검증한다.
     * */
    @Test
    @DisplayName("TemplateType이 Advertise이면 EmailTemplate제목에 '(광고)' 문구가 붙는다.")
    void shouldReturnTitlePrefixAdvertise_whenTemplateTypeIsAdvertise() {
//        // Given
//        EmailSendCreateRequest command = EmailMessageCommandBuilder.builder().build();
//        EmailTemplateDetailResponse emailTemplateResponse = givenEmailTemplateDetailResponse(getContent(BODY));
//
//        givenTemplateClient(emailTemplateResponse);
//        givenAttachmentHandler(null, List.of());
//        givenTemplateAssembler(emailTemplateResponse, emailTemplateResponse.getBody().content());
//
//        EmailTitle templateTypeContent = givenTemplateTypeResolver("(광고) ", "메시지 제목");
//
//        // When
//        SendMessage result = processor.createMessage(command);
//
//        // Then
//        String expected = templateTypeContent.prefix();
//        assertThat(result.getTitle()).startsWith(expected).endsWith(emailTemplateResponse.msgTitle());
    }

    /**
     * Given
     *  - TemplateClient 응답 값으로 BODY, COVER를 준다.
     *  - Body가 Attachment로 변환한 결과 : Body Handler 실행 결과로 NULL을 준다.
     *
     * Then
     *  - SendMessage의 template 내용에 BODY 내용이 포함되는지 검증한다.
     *  - TemplateAssembler의 assemble에 BODY내용으로 BODY를 매개변수로 전달하는지 검증한다.
     * */
    @Test
    @DisplayName("Body가 Attachment로 변환한 결과가 NULL이면 템플릿 내용에는 BODY 내용이 포함된다.")
    void shouldReturnTemplateContainsBody_whenBodyConvertToNull(){
        // Given
//        EmailSendCreateRequest command = EmailMessageCommandBuilder.builder().build();
//        EmailContentResponse body = getContent(BODY);
//        EmailTemplateDetailResponse emailTemplateResponse =
//                givenEmailTemplateDetailResponse(body, getContent(COVER));
//
//        givenTemplateClient(emailTemplateResponse);
//        EmailTitle content = givenTemplateTypeResolver("(광고) ", "메시지 제목");
//        givenTemplateAssembler(emailTemplateResponse, body.content());
//
//        givenAttachmentHandler(null, List.of());

        // When
//        SendMessage result = processor.createMessage(command);
//
//        // Then
//        String expected = body.content();
//        assertThat(result.getTemplate()).contains(expected);
//        verify(assembler).assemble(content, emailTemplateResponse, body);
    }

    /**
     * Given
     *  - TemplateClient 응답 값으로 BODY, COVER를 준다.
     *  - Body가 Attachment로 변환한 결과 : Body Handler 실행 결과로 Attachment를 반환해준다.
     *
     * Then
     *  - SendMessage의 template 내용에 COVER 내용이 포함되는지 검증한다.
     *  - TemplateAssembler의 assemble에 BODY내용으로 COVER를 매개변수로 전달하는지 검증한다.
     * */
    @Test
    @DisplayName("Body가 Attachment로 변환한 결과가 NULL이 아니면 템플릿 내용에는 COVER 내용이 포함된다.")
    void shouldReturnTemplateContainsCover_whenBodyConvertToNotNull(){
//        EmailSendCreateRequest command = EmailMessageCommandBuilder.builder().build();
//        EmailContentResponse body = getContent(BODY);
//        EmailContentResponse cover = getContent(COVER);
//        EmailTemplateDetailResponse emailTemplateResponse =
//                givenEmailTemplateDetailResponse(body, cover);
//
//        givenTemplateClient(emailTemplateResponse);
//        EmailTitle content = givenTemplateTypeResolver("(광고) ", "메시지 제목");
//        givenTemplateAssembler(emailTemplateResponse, cover.content());
//        givenAttachmentHandler(Attachment.of(fromEnumMapperType(PDF)), List.of());
//
//        SendMessage result = processor.createMessage(command);
//        String expected = cover.content();
//
//        assertThat(result.getTemplate()).contains(expected);
//        verify(assembler).assemble(content, emailTemplateResponse, cover);
    }

    /**
     * Given
     *  - TemplateClient 응답 값으로 BODY와 ATTACHMENT 리스트를 준다.
     *  - Body가 Attachment로 변환한 결과 : Body Handler 실행 결과로 NULL을 반환해준다.
     *
     * Then
     *  - Handler BODY 매개 변수에 NULL이 들어가는지 검증한다.
     *  - Handler AttachmentList에 TemplateClient에 반환한 AttachmentList가 들어가는지 검증한다.
     *  - SendMessage의 Attachment List에 TemplateClient에 반환한 AttachmentList의 개수와 동일한지 검증하낟.
     * */
    @Test
    @DisplayName("Body가 Attachment로 변환한 결과가 NULL이면 SendMessage는 첨부파일만 추가된다.")
    void shouldAddAttachmentOnly_whenBodyConvertToNull(){
//        EmailSendCreateRequest command = EmailMessageCommandBuilder.builder().build();
//        EmailContentResponse body = getContent(BODY);
//        EmailTemplateDetailResponse emailTemplateResponse =
//                givenEmailTemplateDetailResponse(body, getContent(COVER), getContent(ATTACHMENT));
//        List<EmailContentResponse> attachments = emailTemplateResponse.getAttachmentList();
//
//        givenTemplateClient(emailTemplateResponse);
//        givenTemplateTypeResolver("(광고) ", "메시지 제목");
//        givenTemplateAssembler(emailTemplateResponse, body.content());
//
//        List<Attachment> attachmentList = List.of(Attachment.of(fromEnumMapperType(NONE)));
//        givenAttachmentHandler(null, attachmentList);
//
//        EmailSendMessage result = (EmailSendMessage) processor.createMessage(command);
//
//        assertThat(result.getAttachments().size()).isEqualTo(attachments.size());
//
//        verify(handler).handle(command, null, attachments);
    }


    /**
     * Given
     *  - TemplateClient 응답 값으로 BODY와 ATTACHMENT 리스트를 준다.
     *  - Body가 Attachment로 변환한 결과 : Body Handler 실행 결과로 Attachment를 반환해준다.
     *
     * Then
     *  - Handler BODY 매개 변수에 반환한 Attachment가 들어가는지 검증한다.
     *  - Handler AttachmentList에 TemplateClient에 반환한 AttachmentList가 들어가는지 검증한다.
     *  - SendMessage의 Attachment List에 TemplateClient에 반환한 AttachmentList의 개수와 변환한 BODY의 Attachment와 동일한지 검증한다.
     * */
    @Test
    @DisplayName("Body가 Attachment로 변환한 결과가 NULL이 아니면 SendMessage는 변환된 BODY와 첨부파일만 추가된다.")
    void shouldAddAttachmentWithBody_whenBodyConvertToNotNull(){
//        EmailSendCreateRequest command = EmailMessageCommandBuilder.builder().build();
//        EmailContentResponse body = getContent(BODY);
//        EmailTemplateDetailResponse emailTemplateResponse =
//                givenEmailTemplateDetailResponse(body, getContent(COVER), getContent(ATTACHMENT));
//        List<EmailContentResponse> attachments = emailTemplateResponse.getAttachmentList();
//
//        givenTemplateClient(emailTemplateResponse);
//        givenTemplateTypeResolver("(광고) ", "메시지 제목");
//        givenTemplateAssembler(emailTemplateResponse, body.content());
//
//        Attachment fromBody = Attachment.of(fromEnumMapperType(PDF));
//        List<Attachment> attachmentList = List.of(Attachment.of(fromEnumMapperType(NONE)));
//        givenAttachmentHandler(fromBody, attachmentList);
//
//        EmailSendMessage result = (EmailSendMessage) processor.createMessage(command);
//
//        assertThat(result.getAttachments().size()).isEqualTo(attachments.size()+1);
//
//        verify(handler).handle(command, fromBody, attachments);
    }

//    private TemplateResponse givenTemplateResponse(TemplateTypeEnum templateTypeEnum) {
//        return new TemplateResponse(
//                UUID.randomUUID().toString(),
//                null,
//                templateTypeEnum.value(),
//                ChannelTypeEnum.EMAIL.value()
//        );
//    }

//    private EmailTemplateDetailResponse givenEmailTemplateDetailResponse(EmailContentResponse... contents) {
//         return new EmailTemplateDetailResponse(
//                UUID.randomUUID().toString(),
//                "메시지 제목",
//                "template/email/images",
//                 Arrays.stream(contents).toList()
//        );
//    }
//    private void givenTemplateAssembler(EmailTemplateDetailResponse template, String body) {
//        EmailContentDto content = new EmailContentDto(null, EmailContent.of(fromEnumMapperType(TEXT), body), null);
//        when(assembler.assemble(any(), any(), any()))
//                .thenReturn(EmailTemplate.of(template.emailContentId(), content));
//    }
//
//    private void givenAttachmentHandler(Attachment body, List<Attachment> attachments) {
//        List<Attachment> attachmentList = Stream.concat(
//                Optional.ofNullable(body)
//                        .map(Stream::of)
//                        .orElseGet(Stream::empty),
//                attachments.stream()
//        ).toList();
//
//        when(handler.handle(any(), any())).thenReturn(body);
//        when(handler.handle(any(), any(), any())).thenReturn(attachmentList);
//    }
//
//    private EmailTitle givenTemplateTypeResolver(String prefix, String title) {
//        when(factory.findEnumMapperValue(any(), any())).thenReturn(fromEnumMapperType(ADVERTISE));
//        EmailTitle templateTypeContent = ofWithPrefix(fromEnumMapperType(ADVERTISE), prefix, title);
//        when(resolver.appendPrefixTexture(any())).thenReturn(templateTypeContent);
//        return templateTypeContent;
//    }
//
//    private void givenTemplateClient(EmailTemplateDetailResponse emailTemplate) {
//        TemplateResponse templateResponse = givenTemplateResponse(ADVERTISE);
//        EmailTemplateResponse template = new EmailTemplateResponse(templateResponse, emailTemplate);
//        when(templateClient.getTemplate(any())).thenReturn(template);
//    }
//
//
//    private EmailContentResponse getContent(EmailTemplateSectionEnum section) {
//        return EmailContentResponseBuilder.builder()
//                .section(section)
//                .content(section.description())
//                .build();
//    }
}