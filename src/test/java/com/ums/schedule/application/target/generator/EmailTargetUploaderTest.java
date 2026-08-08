package com.ums.schedule.application.target.generator;

import com.ums.schedule.application.sendrequest.target.SendTargetUploadService;
import com.ums.schedule.application.target.exception.TargetUploadReportNotConfiguredException;
import com.ums.schedule.application.target.reader.model.TargetRowResult;
import com.ums.schedule.application.target.uploader.EmailTargetUploader;
import com.ums.schedule.application.ums.common.template.loader.EmailTemplateLoader;
import com.ums.schedule.application.ums.common.template.loader.model.EmailTemplate;
import com.ums.schedule.application.ums.email.exception.EmailMessageNotFoundException;
import com.ums.schedule.application.ums.email.generator.EmailTargetMessageGenerator;
import com.ums.schedule.common.code.common.ChannelType;
import com.ums.schedule.common.code.mapper.EnumMapperValue;
import com.ums.schedule.common.code.target_upload.TargetUploadStatus;
import com.ums.schedule.config.properties.TargetUploadProperties;
import com.ums.schedule.domain.message.email.EmailSendMessage;
import com.ums.schedule.domain.message.email.EmailSendMessageJpaRepository;
import com.ums.schedule.domain.target.upload.TargetUploadReport;
import com.ums.schedule.domain.target.upload.state.TargetUploadRequestState;
import com.ums.schedule.fixture.entity.TargetUploadReportEntityBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmailTargetUploaderTest {
    @Mock private TargetUploadProperties properties;
    @Mock private EmailSendMessageJpaRepository messageRepository;
    @Mock private EmailTemplateLoader templateLoader;
    @Mock private SendTargetUploadService targetUploadService;
    @Mock private EmailTargetMessageGenerator generator;

    @InjectMocks private EmailTargetUploader targetUploader;

    private TargetUploadReport targetUploadReport;

    @BeforeEach
    void setUp() {
        targetUploadReport = TargetUploadReportEntityBuilder.builder()
                .uploadStatus(new TargetUploadRequestState())
                .build()
        ;
    }


    @Nested
    @DisplayName("예외 발생 시 ")
    class WhenThrowException {
        @Test
        @DisplayName("이메일 메시지가 존재하지 않으면 예외가 발생한다.")
        void shouldThrowException_whenEmailSendMessageDoesNotExist() {
            doReturn(Optional.empty()).when(messageRepository).findById(any());

            assertThatThrownBy(() -> targetUploader.upload(targetUploadReport, UUID.randomUUID()))
                    .isInstanceOf(EmailMessageNotFoundException.class);
        }

        @Test
        @DisplayName("등록할 분할 개수가 존재하지 않을 시 예외를 던진다. ")
        void shouldChangeStateToFail_whenPartitionSizeIsZero() {
            doReturn(Optional.of(mock(EmailSendMessage.class))).when(messageRepository).findById(any());
            doReturn(mock(EmailTemplate.class)).when(templateLoader).loadTemplate(any());
            doReturn(0).when(properties).getPartitionSize();

            assertThatThrownBy(() -> targetUploader.upload(targetUploadReport, UUID.randomUUID()))
                    .isInstanceOf(TargetUploadReportNotConfiguredException.class);

        }
    }
    @Test
    @DisplayName("채널 타입이 이메일일 때 지원한다.")
    void shouldSupport_whenChannelTypeIsEmail() {
        boolean result = targetUploader.supports(EnumMapperValue.fromEnumMapperType(ChannelType.EMAIL));

        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("messageId로 이메일 메시지를 조회한다.")
    void shouldFindEmailSendMessage() {
        doReturn(Optional.of(mock(EmailSendMessage.class))).when(messageRepository).findById(any());
        doReturn(mock(EmailTemplate.class)).when(templateLoader).loadTemplate(any());
        doReturn(100).when(properties).getPartitionSize();

        Consumer<List<TargetRowResult>> results = targetUploader.upload(targetUploadReport, UUID.randomUUID());

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
            targetUploader.upload(targetUploadReport, UUID.randomUUID());

            verify(templateLoader).loadTemplate(any());
        }

        @Test
        @DisplayName("대상자 업로드 상태는 PARSING으로 변경된다.")
        void shouldChangeStateToParsing() {
            targetUploader.upload(targetUploadReport, UUID.randomUUID());

            assertThat(targetUploadReport.getState().getCurrentCode())
                    .isEqualTo(TargetUploadStatus.PARSING);
        }


        @Test
        @DisplayName("설정 파일에서 등록할 분할 개수를 조회한다.")
        void shouldGetPartitionSize() {
            targetUploader.upload(targetUploadReport, UUID.randomUUID());
            verify(properties).getPartitionSize();
        }

        @Test
        @DisplayName("대상자 생성은 대상자 로우만큼 실행된다.")
        void shouldExecuteTargetCreateService() {
            TargetRowResult row = mock(TargetRowResult.class);
            List<TargetRowResult> results = List.of(row, row, row);

            givenTargetList(results);

            verify(generator, times(3)).generate(any(), any());

        }

        @Test
        @DisplayName("대상자 업로드가 실행된다.")
        void shouldExecuteTargetUpload() {
            TargetRowResult row = mock(TargetRowResult.class);
            List<TargetRowResult> results = List.of(row, row, row);

            givenTargetList(results);

            verify(targetUploadService).upload(any(), anyInt());
        }

        private void givenTargetList(List<TargetRowResult> results) {
            Consumer<List<TargetRowResult>> consumer = targetUploader.upload(targetUploadReport, UUID.randomUUID());
            consumer.accept(results);
        }
    }

    private void givenInvokeServices() {
        doReturn(Optional.of(mock(EmailSendMessage.class))).when(messageRepository).findById(any());
        doReturn(mock(EmailTemplate.class)).when(templateLoader).loadTemplate(any());
        doReturn(100).when(properties).getPartitionSize();
    }
}