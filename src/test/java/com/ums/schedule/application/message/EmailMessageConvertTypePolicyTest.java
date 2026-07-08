package com.ums.schedule.application.message;

import com.ums.schedule.application.resource.email.command.EmailConvertPolicyCommandBuilder;
import com.ums.schedule.application.sendrequest.message.email.command.EmailAttachmentCreateCommand;
import com.ums.schedule.application.sendrequest.message.email.command.EmailConvertPolicyCommand;
import com.ums.schedule.application.sendrequest.message.email.policy.EmailMessageConvertTypePolicy;
import com.ums.schedule.application.sendrequest.message.email.result.EmailMessagePolicyResult;
import com.ums.schedule.application.template.email.query.model.EmailTemplateContentResult;
import com.ums.schedule.fixture.template.EmailContentResultBuilder;
import com.ums.schedule.common.code.mapper.EnumMapperFactory;
import com.ums.schedule.common.code.mapper.EnumMapperValue;
import com.ums.schedule.domain.sendrequest.resource.email.code.ConvertTypeEnum;
import com.ums.schedule.domain.sendrequest.template.email.code.EmailTemplateSectionEnum;
import com.ums.schedule.domain.sendrequest.template.code.TemplateEnumMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class EmailMessageConvertTypePolicyTest {
    @Mock private EnumMapperFactory mapperFactory;
    @InjectMocks private EmailMessageConvertTypePolicy convertPolicy;
    private EmailConvertPolicyCommandBuilder builder;

    @Nested
    @DisplayName("CONVERT_TYPE이 NULL일 때")
    class WhenConvertTypeIsNull {
        @BeforeEach
        void setUp() {
            builder = EmailConvertPolicyCommandBuilder.builder().convertType(null);
        }

        @Test
        @DisplayName("security_policy가 NULL이면 convert_type은 NONE이 반환된다.")
        void shouldReturnNone_whenSecurityPolicyIsNull() {
            EmailConvertPolicyCommand givenCommand = builder.build();
            EmailMessagePolicyResult result = convertPolicy.generateConvertPolicy(givenCommand);
            assertThat(result.convertType()).isEqualTo(EnumMapperValue.fromEnumMapperType(ConvertTypeEnum.NONE));
        }

        @Test
        @DisplayName("security_policy가 존재하면 convert_type은 HTML이 반환된다.")
        void shouldReturnHtml_whenSecurityPolicyExists() {
            EmailConvertPolicyCommand givenCommand = builder.hasSecurityPolicy(true).build();
            EmailMessagePolicyResult result = convertPolicy.generateConvertPolicy(givenCommand);
            assertThat(result.convertType()).isEqualTo(EnumMapperValue.fromEnumMapperType(ConvertTypeEnum.HTML));
        }
    }

    @Nested
    @DisplayName("convert_type이 NONE일 때")
    class WhenConvertTypeIsNone {
        @Test
        @DisplayName("attachment 개수가 유지된다")
        void shouldKeepAttachmentCount_whenConvertTypeIsNone() {
            builder = EmailConvertPolicyCommandBuilder.builder().convertType(null);

            EmailTemplateContentResult attachmentA = EmailContentResultBuilder.builder().attachment().build();
            EmailTemplateContentResult attachmentB = EmailContentResultBuilder.builder().attachment().build();
            EmailTemplateContentResult attachmentC = EmailContentResultBuilder.builder().attachment().build();

            EmailConvertPolicyCommand givenCommand = builder.attachmentList(List.of(attachmentA, attachmentB, attachmentC)).build();

            EmailMessagePolicyResult result = convertPolicy.generateConvertPolicy(givenCommand);
            assertThat(result.attachmentList()).hasSize(3);
        }
    }

    @Nested
    @DisplayName("convert_type이 HTML일 때")
    class WhenConvertTypeIsHtml {

        @BeforeEach
        void setUp() {
            builder = EmailConvertPolicyCommandBuilder.builder().convertType("HTML")
                    .body(
                            EmailContentResultBuilder.builder()
                                    .fileKey("body.html")
                                    .build()
                    )
                    .cover(
                            EmailContentResultBuilder.builder().cover()
                                    .fileKey("cover.html")
                                    .build()
                    )
            ;

            doReturn(EnumMapperValue.fromEnumMapperType(ConvertTypeEnum.HTML))
                    .when(mapperFactory).findEnumMapperValue(TemplateEnumMapper.CONVERT_TYPE, "HTML");
        }

        @Test
        @DisplayName("cover file_key를 반환한다")
        void shouldReturnCoverFileKey_whenConvertTypeIsHtml() {
            EmailConvertPolicyCommand givenCommand =
                    builder.cover(
                        EmailContentResultBuilder.builder().cover()
                                .fileKey("cover.html")
                                .build()
                        ).build();

            EmailMessagePolicyResult result = convertPolicy.generateConvertPolicy(givenCommand);
            Map<EmailTemplateSectionEnum, String> expectMap = result.fileKeyMap();

            assertThat(expectMap).hasEntrySatisfying(
                    EmailTemplateSectionEnum.BODY,
                    value -> assertThat(value).isEqualTo("cover.html")
            );
        }

        @Test
        @DisplayName("attachment가 1개 추가된다")
        void shouldAddOneAttachment_whenConvertTypeIsHtml() {
            EmailTemplateContentResult attachmentA = EmailContentResultBuilder.builder().attachment().build();
            EmailTemplateContentResult attachmentB = EmailContentResultBuilder.builder().attachment().build();
            EmailTemplateContentResult attachmentC = EmailContentResultBuilder.builder().attachment().build();

            EmailConvertPolicyCommand givenCommand =
                    builder.cover(
                            EmailContentResultBuilder.builder().cover()
                                    .fileKey("cover.html")
                                    .build()
                    )
                            .attachmentList(List.of(attachmentA, attachmentB, attachmentC))
                            .build();

            EmailMessagePolicyResult result = convertPolicy.generateConvertPolicy(givenCommand);

            assertThat(result.attachmentList()).hasSize(4);
        }

        @Test
        @DisplayName("HTML attachment를 생성한다")
        void shouldCreateHtmlAttachment_whenConvertTypeIsHtml() {
            EmailTemplateContentResult attachmentA = EmailContentResultBuilder.builder().attachment().build();
            EmailTemplateContentResult attachmentB = EmailContentResultBuilder.builder().attachment().build();
            EmailTemplateContentResult attachmentC = EmailContentResultBuilder.builder().attachment().build();

            EmailConvertPolicyCommand givenCommand =
                    builder.cover(
                                    EmailContentResultBuilder.builder().cover()
                                            .fileKey("cover.html")
                                            .build()
                            )
                            .attachmentList(List.of(attachmentA, attachmentB, attachmentC))
                            .build();

            EmailMessagePolicyResult result = convertPolicy.generateConvertPolicy(givenCommand);
            List<EmailAttachmentCreateCommand> expectList = result.attachmentList();

            assertThat(expectList).anySatisfy(expect -> {
                assertThat(expect.convertType())
                        .isEqualTo(EnumMapperValue.fromEnumMapperType(ConvertTypeEnum.HTML));
            });
        }

        @Test
        @DisplayName("생성된 HTML attachment의 file_key는 body file_key이다")
        void shouldUseBodyFileKeyForHtmlAttachment() {
            EmailTemplateContentResult attachmentA = EmailContentResultBuilder.builder().attachment().build();
            EmailTemplateContentResult attachmentB = EmailContentResultBuilder.builder().attachment().build();
            EmailTemplateContentResult attachmentC = EmailContentResultBuilder.builder().attachment().build();

            EmailConvertPolicyCommand givenCommand =
                    builder.cover(
                                    EmailContentResultBuilder.builder().cover()
                                            .fileKey("cover.html")
                                            .build()
                            )
                            .attachmentList(List.of(attachmentA, attachmentB, attachmentC))
                            .build();

            EmailMessagePolicyResult result = convertPolicy.generateConvertPolicy(givenCommand);
            List<EmailAttachmentCreateCommand> expectList = result.attachmentList();

            assertThat(expectList)
                    .filteredOn(command -> command.convertType().code().equals(ConvertTypeEnum.HTML.code()))
                    .hasSize(1)
                    .first()
                    .extracting(EmailAttachmentCreateCommand::fileKey)
                    .isEqualTo("body.html");
        }
    }

    @Nested
    @DisplayName("convert_type이 PDF일 때")
    class WhenConvertTypeIsPdf  {

        @BeforeEach
        void setUp() {
            builder = EmailConvertPolicyCommandBuilder.builder()
                    .convertType("PDF")
                    .body(
                            EmailContentResultBuilder.builder()
                                    .fileKey("body.html")
                                    .build()
                    )
                    .cover(
                            EmailContentResultBuilder.builder().cover()
                                    .fileKey("cover.html")
                                    .build()
                    );

            doReturn(EnumMapperValue.fromEnumMapperType(ConvertTypeEnum.PDF))
                    .when(mapperFactory).findEnumMapperValue(TemplateEnumMapper.CONVERT_TYPE, "PDF");
        }

        @Test
        @DisplayName("cover file_key를 반환한다")
        void shouldReturnCoverFileKey_whenConvertTypeIsPdf() {
            EmailConvertPolicyCommand givenCommand =
                    builder.cover(
                            EmailContentResultBuilder.builder().cover()
                                    .fileKey("cover.html")
                                    .build()
                    ).build();

            EmailMessagePolicyResult result = convertPolicy.generateConvertPolicy(givenCommand);
            Map<EmailTemplateSectionEnum, String> expectMap = result.fileKeyMap();

            assertThat(expectMap).hasEntrySatisfying(
                    EmailTemplateSectionEnum.BODY,
                    value -> assertThat(value).isEqualTo("cover.html")
            );
        }

        @Test
        @DisplayName("attachment가 1개 추가된다")
        void shouldAddOneAttachment_whenConvertTypeIsPdf() {
            EmailTemplateContentResult attachmentA = EmailContentResultBuilder.builder().attachment().build();
            EmailTemplateContentResult attachmentB = EmailContentResultBuilder.builder().attachment().build();
            EmailTemplateContentResult attachmentC = EmailContentResultBuilder.builder().attachment().build();

            EmailConvertPolicyCommand givenCommand =
                    builder.cover(
                                    EmailContentResultBuilder.builder().cover()
                                            .fileKey("cover.html")
                                            .build()
                            )
                            .attachmentList(List.of(attachmentA, attachmentB, attachmentC))
                            .build();

            EmailMessagePolicyResult result = convertPolicy.generateConvertPolicy(givenCommand);

            assertThat(result.attachmentList()).hasSize(4);
        }

        @Test
        @DisplayName("PDF attachment를 생성한다")
        void shouldCreatePdfAttachment_whenConvertTypeIsPdf() {
            EmailTemplateContentResult attachmentA = EmailContentResultBuilder.builder().attachment().build();
            EmailTemplateContentResult attachmentB = EmailContentResultBuilder.builder().attachment().build();
            EmailTemplateContentResult attachmentC = EmailContentResultBuilder.builder().attachment().build();

            EmailConvertPolicyCommand givenCommand =
                    builder.cover(
                                    EmailContentResultBuilder.builder().cover()
                                            .fileKey("cover.html")
                                            .build()
                            )
                            .attachmentList(List.of(attachmentA, attachmentB, attachmentC))
                            .build();

            EmailMessagePolicyResult result = convertPolicy.generateConvertPolicy(givenCommand);
            List<EmailAttachmentCreateCommand> expectList = result.attachmentList();

            assertThat(expectList).anySatisfy(expect -> {
                assertThat(expect.convertType())
                        .isEqualTo(EnumMapperValue.fromEnumMapperType(ConvertTypeEnum.PDF));
            });
        }

        @Test
        @DisplayName("생성된 PDF attachment의 file_key는 body file_key이다")
        void shouldUseBodyFileKeyForPdfAttachment() {
            EmailConvertPolicyCommand givenCommand =
                    builder.cover(
                            EmailContentResultBuilder.builder().cover()
                                    .fileKey("cover.html")
                                    .build()
                    ).build();

            EmailMessagePolicyResult result = convertPolicy.generateConvertPolicy(givenCommand);
            Map<EmailTemplateSectionEnum, String> expectMap = result.fileKeyMap();

            assertThat(expectMap).hasEntrySatisfying(
                    EmailTemplateSectionEnum.BODY,
                    value -> assertThat(value).isEqualTo("cover.html")
            );
        }
    }
}