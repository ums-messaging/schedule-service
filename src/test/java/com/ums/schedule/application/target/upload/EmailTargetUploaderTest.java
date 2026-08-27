package com.ums.schedule.application.target.upload;

import com.ums.schedule.application.target.exception.TargetUploadReportNotFoundException;
import com.ums.schedule.application.target.uploader.EmailTargetUploadWorker;
import com.ums.schedule.application.target.uploader.EmailTargetUploader;
import com.ums.schedule.application.ums.common.template.loader.EmailTemplateLoader;
import com.ums.schedule.application.ums.common.template.loader.model.EmailTemplate;
import com.ums.schedule.application.ums.email.exception.EmailMessageNotFoundException;
import com.ums.schedule.common.code.common.ChannelType;
import com.ums.schedule.common.code.mapper.EnumMapperValue;
import com.ums.schedule.domain.message.email.EmailSendMessage;
import com.ums.schedule.domain.message.email.EmailSendMessageJpaRepository;
import com.ums.schedule.domain.target.upload.TargetUploadReport;
import com.ums.schedule.domain.target.upload.TargetUploadReportJpaRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmailTargetUploaderTest {
    @Mock private TargetUploadReportJpaRepository targetUploadReportRepository;
    @Mock private EmailSendMessageJpaRepository messageRepository;
    @Mock private EmailTemplateLoader templateLoader;
    @Mock private EmailTargetUploadWorker worker;


    private EmailTargetUploader targetUploader;

    @BeforeEach
    void setUp() {
        targetUploader = new EmailTargetUploader(
                targetUploadReportRepository,
                messageRepository,
                templateLoader,
                worker
        );
    }

    @Nested
    @DisplayName("예외 발생 시 ")
    class WhenThrowException {
        @Test
        @DisplayName("대상자 업로드 리포트가 존재하지 않으면 예외가 발생한다.")
        void shouldThrowException_whenTargetUploadReportDoesNotExist() {
            doReturn(Optional.empty()).when(targetUploadReportRepository).findById(any());

            assertThatThrownBy(() ->
                    targetUploader.upload(UUID.randomUUID(), UUID.randomUUID())
            ).isInstanceOf(TargetUploadReportNotFoundException.class);
        }
        @Test
        @DisplayName("이메일 메시지가 존재하지 않으면 예외가 발생한다.")
        void shouldThrowException_whenEmailSendMessageDoesNotExist() {
            doReturn(Optional.of(mock(TargetUploadReport.class))).when(targetUploadReportRepository).findById(any());
            doReturn(Optional.empty()).when(messageRepository).findById(any());

            assertThatThrownBy(() ->
                    targetUploader.upload(UUID.randomUUID(), UUID.randomUUID())
            ).isInstanceOf(EmailMessageNotFoundException.class);
        }
    }

    @Test
    @DisplayName("채널 타입이 이메일일 때 지원한다.")
    void shouldSupport_whenChannelTypeIsEmail() {
        boolean result = targetUploader.supports(EnumMapperValue.fromEnumMapperType(ChannelType.EMAIL));

        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("reportId로 대상자 업로드 리포트를 조회한다.")
    void shouldFindTargetUploadReport() {
        doReturn(Optional.of(mock(TargetUploadReport.class))).when(targetUploadReportRepository).findById(any());
        doReturn(Optional.of(mock(EmailSendMessage.class))).when(messageRepository).findById(any());
        doReturn(mock(EmailTemplate.class)).when(templateLoader).loadTemplate(any());

        targetUploader.upload(UUID.randomUUID(), UUID.randomUUID());


        verify(targetUploadReportRepository).findById(any());
    }

    @Test
    @DisplayName("messageId로 이메일 메시지를 조회한다.")
    void shouldFindEmailSendMessage() {
        doReturn(Optional.of(mock(TargetUploadReport.class))).when(targetUploadReportRepository).findById(any());
        doReturn(Optional.of(mock(EmailSendMessage.class))).when(messageRepository).findById(any());
        doReturn(mock(EmailTemplate.class)).when(templateLoader).loadTemplate(any());

        targetUploader.upload(UUID.randomUUID(), UUID.randomUUID());

        verify(messageRepository).findById(any());
    }

    @Nested
    @DisplayName("대상자 업로드가 정상적으로 실행된다.")
    class WhenTargetUpload {
        @BeforeEach
        void setUp() {
            givenInvokeServices();
        }

        @Test
        @DisplayName("템플릿 로더를 실행한다.")
        void shouldExecuteTemplateLoader() {
            targetUploader.upload(UUID.randomUUID(), UUID.randomUUID());

            verify(templateLoader).loadTemplate(any());
        }

    }

    private void givenInvokeServices() {
        doReturn(Optional.of(mock(TargetUploadReport.class))).when(targetUploadReportRepository).findById(any());
        doReturn(Optional.of(mock(EmailSendMessage.class))).when(messageRepository).findById(any());
        doReturn(mock(EmailTemplate.class)).when(templateLoader).loadTemplate(any());
    }
}