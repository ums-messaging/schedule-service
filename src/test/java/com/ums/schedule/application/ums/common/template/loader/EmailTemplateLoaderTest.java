package com.ums.schedule.application.ums.common.template.loader;

import com.ums.schedule.adapter.storage.AwsS3Repository;
import com.ums.schedule.application.ums.common.exception.TemplateLoadFailException;
import com.ums.schedule.application.ums.common.template.loader.model.EmailTemplate;
import com.ums.schedule.domain.message.email.EmailSendMessage;
import com.ums.schedule.domain.request.message.email.EmailSendMessageBuilder;
import freemarker.template.Configuration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import software.amazon.awssdk.services.s3.model.NoSuchKeyException;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmailTemplateLoaderTest {
    @Mock private AwsS3Repository fileRepository;
    @Spy
    private Configuration configuration;
    @InjectMocks private EmailTemplateLoader loader;

    private EmailSendMessageBuilder builder ;

    @BeforeEach
    void setUp() {
        configuration = new Configuration(Configuration.VERSION_2_3_31);
        builder = EmailSendMessageBuilder.builder()
                .id(UUID.randomUUID())
                .bodyTemplateKey("body.html")
        ;
    }

    @Nested
    @DisplayName("헤더 키 테스트")
    class WhenHeaderKey {
        private EmailSendMessage sendMessage;

        @BeforeEach
        void setUp() {
            sendMessage = builder.headerTemplateKey("header.html")
                    .build();
        }

        @Test
        @DisplayName("헤더 템플릿이 반환된다.")
        void shouldReturnHeaderTemplate() {
            doReturn("headerTemplate").when(fileRepository)
                    .getFileStringContent(anyString());

            EmailTemplate template = loader.loadTemplate(sendMessage);

            assertThat(template.getHeader())
                    .extracting(t -> t.template())
                    .isNotNull();
        }

        @Test
        @DisplayName("헤더 키가 존재하면, 헤더 파일을 읽는다.")
        void shouldReadFileContent() {
            ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
            doReturn("headerTemplate").when(fileRepository)
                    .getFileStringContent(anyString());

            loader.loadTemplate(sendMessage);

            verify(fileRepository, times(2)).getFileStringContent(captor.capture());
            assertThat(captor.getAllValues())
                    .extracting(v -> v)
                    .contains("header.html");
        }

        @Test
        @DisplayName("헤더 키가 존재하는데, 파일이 존재하지 않으면 예외가 발생한다.")
        void shouldThrowException_whenFileNotFound() {
            doAnswer(invocation -> {
                 String fileKey = invocation.getArgument(0);
                 if("body.html".equals(fileKey)) {
                    return "template content";
                 }
                 throw mock(NoSuchKeyException.class);
            }).when(fileRepository).getFileStringContent(anyString());

            assertThatThrownBy(() -> loader.loadTemplate(sendMessage))
                    .isInstanceOf(TemplateLoadFailException.class);;
        }

        @Test
        @DisplayName("헤더 키가 존재하지 않으면, 헤더 템플릿은 반환되지 않는다.")
        void shouldNotReturnHeaderTemplate_whenHeaderKeyDoesNotExist() {
            EmailSendMessage sendMessage = builder.headerTemplateKey("").build();
            doReturn("template").when(fileRepository).getFileStringContent(anyString());

            EmailTemplate template = loader.loadTemplate(sendMessage);

            assertThat(template.getHeader()).isNull();
        }
        @Test
        @DisplayName("헤더 키가 존재하는데 파일 내용이 존재하지 않으면 예외가 발생한다.")
        void shouldThrowException_whenFileContentIsEmpty() {
            sendMessage = builder.headerTemplateKey("header.html")
                    .build();
            doReturn("").when(fileRepository).getFileStringContent(anyString());

            assertThatThrownBy(() -> loader.loadTemplate(sendMessage))
                    .isInstanceOf(TemplateLoadFailException.class);
        }
    }

    @Nested
    @DisplayName("푸터 키 테스트")
    class WhenFooterKeyTest {
        private EmailSendMessage sendMessage;

        @BeforeEach
        void setUp() {
            sendMessage = builder.footerTemplateKey("footer.html")
                    .build();
        }

        @Test
        @DisplayName("푸터 템플릿이 반환된다.")
        void shouldReturnHeaderTemplate() {
            doReturn("footerTemplate").when(fileRepository)
                    .getFileStringContent(anyString());

            EmailTemplate template = loader.loadTemplate(sendMessage);

            assertThat(template.getFooter())
                    .extracting(t -> t.template())
                    .isNotNull();
        }

        @Test
        @DisplayName("푸터 파일을 읽는다.")
        void shouldReadFileContent() {
            ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
            doReturn("footerTemplate").when(fileRepository)
                    .getFileStringContent(anyString());

            loader.loadTemplate(sendMessage);

            verify(fileRepository, times(2)).getFileStringContent(captor.capture());
            assertThat(captor.getAllValues())
                    .extracting(v -> v)
                    .contains("footer.html");
        }

        @Test
        @DisplayName("푸터 키가 존재하는데, 파일이 존재하지 않으면 예외가 발생한다.")
        void shouldThrowException_whenFileNotFound() {
            doAnswer(invocation -> {
                String fileKey = invocation.getArgument(0);
                if("body.html".equals(fileKey)) {
                    return "template content";
                }
                throw mock(NoSuchKeyException.class);
            }).when(fileRepository).getFileStringContent(anyString());

            assertThatThrownBy(() -> loader.loadTemplate(sendMessage))
                    .isInstanceOf(TemplateLoadFailException.class);
        }

        @Test
        @DisplayName("푸터 키가 존재하는데 파일 내용이 존재하지 않으면 예외가 발생한다.")
        void shouldThrowException_whenFileContentIsEmpty() {
            sendMessage = builder.footerTemplateKey("footer.html")
                    .build();
            doReturn("").when(fileRepository).getFileStringContent(anyString());

            assertThatThrownBy(() -> loader.loadTemplate(sendMessage))
                    .isInstanceOf(TemplateLoadFailException.class);
        }
    }

    @Nested
    @DisplayName("본문 키 테스트")
    class WhenBodyKey {
        private EmailSendMessage sendMessage;

        @BeforeEach
        void setUp() {
            sendMessage = builder.build();
        }
        @Test
        @DisplayName("본문 템플릿이 반환된다.")
        void shouldReturnBodyTemplate() {
            doReturn("bodyTemplate").when(fileRepository)
                    .getFileStringContent(anyString());

            EmailTemplate template = loader.loadTemplate(sendMessage);

            assertThat(template.getBody())
                    .extracting(t -> t.template())
                    .isNotNull();
        }

        @Test
        @DisplayName("본문 파일을 읽는다.")
        void shouldReadFileContent() {
            ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
            doReturn("bodyTemplate").when(fileRepository)
                    .getFileStringContent(anyString());

            loader.loadTemplate(sendMessage);

            verify(fileRepository).getFileStringContent(captor.capture());
            assertThat(captor.getAllValues())
                    .extracting(v -> v)
                    .contains("body.html");
        }

        @Test
        @DisplayName("본문 키가 존재하는데, 파일이 존재하지 않으면 예외가 발생한다.")
        void shouldThrowException_whenFileNotFound() {
            doThrow(mock(NoSuchKeyException.class)).when(fileRepository).getFileStringContent(anyString());

            assertThatThrownBy(() -> loader.loadTemplate(sendMessage))
                    .isInstanceOf(TemplateLoadFailException.class);;
        }

        @Test
        @DisplayName("본문 키가 존재하는데 파일 내용이 존재하지 않으면 예외가 발생한다.")
        void shouldThrowException_whenFileContentIsEmpty() {
            doReturn("").when(fileRepository).getFileStringContent(anyString());

            assertThatThrownBy(() -> loader.loadTemplate(sendMessage))
                    .isInstanceOf(TemplateLoadFailException.class);
        }
    }

    @Nested
    @DisplayName("커버 키가 존재할 때")
    class WhenCoverKeyExist {
        private EmailSendMessage sendMessage;

        @BeforeEach
        void setUp() {
            sendMessage = builder.coverTemplateKey("cover.html")
                    .build();
        }
        @Test
        @DisplayName("커버 템플릿이 반환된다.")
        void shouldReturnCoverTemplate() {
            doReturn("coverTemplate").when(fileRepository)
                    .getFileStringContent(anyString());

            EmailTemplate template = loader.loadTemplate(sendMessage);

            assertThat(template.getBody())
                    .extracting(t -> t.template())
                    .isNotNull();
        }

        @Test
        @DisplayName("커버 파일을 읽는다.")
        void shouldReadFileContent() {
            ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
            doReturn("coverTemplate").when(fileRepository)
                    .getFileStringContent(anyString());

            loader.loadTemplate(sendMessage);

            verify(fileRepository, times(2)).getFileStringContent(captor.capture());

            assertThat(captor.getAllValues())
                    .extracting(v -> v)
                    .contains("body.html");
        }

        @Test
        @DisplayName("커버 키가 존재하는데, 파일이 존재하지 않으면 예외가 발생한다.")
        void shouldThrowException_whenFileNotFound() {
            doThrow(mock(NoSuchKeyException.class)).when(fileRepository).getFileStringContent(anyString());

            assertThatThrownBy(() -> loader.loadTemplate(sendMessage))
                    .isInstanceOf(TemplateLoadFailException.class);;
        }

        @Test
        @DisplayName("커버 키가 존재하는데 파일 내용이 존재하지 않으면 예외가 발생한다.")
        void shouldThrowException_whenFileContentIsEmpty() {
            doReturn("").when(fileRepository).getFileStringContent(anyString());

            assertThatThrownBy(() -> loader.loadTemplate(sendMessage))
                    .isInstanceOf(TemplateLoadFailException.class);
        }
    }

    @Test
    @DisplayName("첨부 파일 개수만큼 첨부파일 템플릿 정보가 생성된다.")
    void shouldCreateAttachmentList() {

    }

    @Test
    @DisplayName("첨부파일이 존재하지 않으면 첨부파일 개수는 빈 배열이다.")
    void shouldEmptyList_whenAttachmentsEmpty(){

    }
}