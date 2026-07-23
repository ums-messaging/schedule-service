package com.ums.schedule.application.sendrequest;

import com.ums.schedule.adapter.storage.AwsS3Repository;
import com.ums.schedule.adapter.storage.PresigendUrlResponse;
import com.ums.schedule.application.target.upload.exception.TargetUploadProcessException;
import com.ums.schedule.application.target.upload.handler.FileTargetUploadResult;
import com.ums.schedule.application.target.upload.handler.FileTargetUploadUrlProvider;
import com.ums.schedule.common.code.api.FileErrorCode;
import com.ums.schedule.common.code.api.TargetUploadErrorCode;
import com.ums.schedule.common.exception.file.AmazonS3FileException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import software.amazon.awssdk.services.s3.model.NoSuchKeyException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FileTargetUploadUrlProviderTest {
    @Mock private AwsS3Repository repository;
    @InjectMocks private FileTargetUploadUrlProvider provider;


    @Test
    @DisplayName("presined_url을 발급한다.")
    void shouldCreatePresignedUrl() {
        doReturn(mock(PresigendUrlResponse.class)).when(repository).generateUploadUrl(anyString());

        FileTargetUploadResult result = provider.provide("/target/upload/id.xlsx");

        assertThat(result).isNotNull();
        verify(repository).generateUploadUrl("/target/upload/id.xlsx");
    }

    @Test
    @DisplayName("presigned_url발급 오류 시 예외가 변환된다.")
    void shouldConvertThrowException() {
        NoSuchKeyException exception = mock(NoSuchKeyException.class);
        doThrow(exception).when(repository).generateUploadUrl(anyString());

        TargetUploadProcessException expect = TargetUploadProcessException.of(TargetUploadErrorCode.UPLOAD_KEY_GENERATION_FAILED, exception);

        assertThatThrownBy(() -> provider.provide("/target/upload/id.xlsx"))
                .isInstanceOf(expect.getClass())
                .hasMessage(expect.getMessage())
        ;

    }
}