package com.ums.schedule.application.sendrequest;

import com.ums.schedule.adapter.storage.AwsS3Repository;
import com.ums.schedule.adapter.storage.PresigendUrlResponse;
import com.ums.schedule.application.exception.FileStorageException;
import com.ums.schedule.application.sendrequest.target.result.FileTargetUploadResult;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import software.amazon.awssdk.services.s3.model.NoSuchKeyException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
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

        FileStorageException expect = FileStorageException.of(exception);

        assertThatThrownBy(()->provider.provide("/target/upload/id.xlsx"))
                .isInstanceOf(expect.getClass())
                .hasMessage(expect.getMessage())
        ;

    }
}