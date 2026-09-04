package com.ums.schedule.application.ums.email.generator.resolver;


import com.ums.schedule.application.sendrequest.target.data.TargetMessageData;
import com.ums.schedule.application.ums.email.exception.EmailConvertTypeNotSupportedException;
import com.ums.schedule.application.ums.email.generator.policy.AttachmentEmailConvertPolicy;
import com.ums.schedule.application.ums.email.generator.policy.EmailMessageConvertPolicy;
import com.ums.schedule.application.ums.email.generator.policy.IdentityEmailConvertPolicy;
import com.ums.schedule.application.ums.email.generator.policy.model.EmailConvertPolicy;
import com.ums.schedule.common.code.email.ConvertType;
import com.ums.schedule.fixture.email.EmailTemplateBuilder;
import org.junit.jupiter.api.BeforeEach;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class EmailConvertResolverTest {

    @Mock private IdentityEmailConvertPolicy identityPolicy;
    @Mock private AttachmentEmailConvertPolicy attachmentPolicy;

    @Spy private List<EmailMessageConvertPolicy> polices = new ArrayList<>();
    @InjectMocks private EmailConvertResolver resolver;

    private EmailTemplateBuilder templateBuilder;

    @BeforeEach
    void setUp() {
        polices.add(identityPolicy);
        polices.add(attachmentPolicy);

        templateBuilder = EmailTemplateBuilder.builder()
                .convertType(ConvertType.NONE);
    }

    @Test
    @DisplayName("AttachmentConvertPolicy가 실행된다.")
    void shouldExecuteAttachmentConvertPolicy() {
        doReturn(true).when(attachmentPolicy).supports(any());
        doReturn(mock(EmailConvertPolicy.class)).when(attachmentPolicy).convert(any(), any());

        resolver.resolve(templateBuilder.build(), mock(TargetMessageData.class));

        verify(attachmentPolicy).convert(any(), any());
    }

    @Test
    @DisplayName("IdentityConvertPolicy가 실행된다.")
    void shouldInvokeIdentityConvertPolicy_whenConvertTypeIsNone() {
        doReturn(true).when(identityPolicy).supports(any());
        doReturn(mock(EmailConvertPolicy.class)).when(identityPolicy).convert(any(), any());

        resolver.resolve(templateBuilder.build(), mock(TargetMessageData.class));

        verify(identityPolicy).convert(any(), any());
    }

    @Test
    @DisplayName("지원하는 변환 타입이 없을 경우, 예외가 발생한다.")
    void shouldThrowException_whenNotSupportedConvertType(){
        doReturn(false).when(identityPolicy).supports(any());
        doReturn(false).when(attachmentPolicy).supports(any());

        assertThatThrownBy(() -> resolver.resolve(templateBuilder.build(), mock(TargetMessageData.class)))
                .isInstanceOf(EmailConvertTypeNotSupportedException.class);
    }
}