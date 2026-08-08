package com.ums.schedule.application.target.reader;

import com.alibaba.excel.EasyExcel;
import com.ums.schedule.application.target.reader.model.TargetRowResult;
import com.ums.schedule.common.code.target.SendTargetRowStatus;
import com.ums.schedule.fixture.target_upload.util.TargetUploadCreateFileUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.function.Consumer;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TargetUploadFileReaderListenerTest {
    @Mock private Consumer<List<TargetRowResult>> consumer;
    private TargetUploadFileReaderListener listener;

    @TempDir
    private Path temp;
    private InputStream is;

    @BeforeEach
    void setUp() throws IOException {
        temp = TargetUploadCreateFileUtils.createTargetList(100);
        is = Files.newInputStream(temp);
    }
    @Nested
    class WhenReadSuccess {
        @BeforeEach
        void setUp() {

            listener = new TargetUploadFileReaderListener(
                    consumer,
                    10
            );
            EasyExcel.read(is, listener)
                    .headRowNumber(1)
                    .sheet()
                    .doRead();
        }

        @Test
        @DisplayName("대상자 파싱에 성공하면 SUCCESS 상태를 저장한다.")
        void shouldAddSuccessTargetRowResult_whenParsingSucceeds() {
            ArgumentCaptor<List<TargetRowResult>> captor = ArgumentCaptor.forClass(List.class);

            verify(consumer, times(10)).accept(captor.capture());

            List<List<TargetRowResult>> allValues = captor.getAllValues();

            assertThat(allValues)
                    .flatExtracting(batch -> batch)
                    .extracting(TargetRowResult::status)
                    .containsOnly(SendTargetRowStatus.SUCCESS);
        }
        @Test
        @DisplayName("대상자 목록이 배치 사이즈에 도달하면 업로더를 호출한다.")
        void shouldInvokeConsumer_whenBatchSizeReached() {
            verify(consumer, times(10)).accept(any());
        }

        @Test
        @DisplayName("Consumer에 배치 크기만큼의 대상자를 전달한다.")
        void shouldPassBatchSizeTargetsToConsumer() {
            ArgumentCaptor<List<TargetRowResult>> captor = ArgumentCaptor.forClass(List.class);

            verify(consumer, times(10)).accept(captor.capture());

            List<List<TargetRowResult>> captors = captor.getAllValues();

            assertThat(captors)
                    .allSatisfy(batch -> assertThat(batch).hasSize(10));
        }
    }

    @Test
    @DisplayName("분석 종료 후 남은 대상자를 Consumer에 전달한다.")
    void shouldInvokeConsumerWithRemainingTargets_whenRemainingTargetsExist() {
        ArgumentCaptor<List<TargetRowResult>> captor = ArgumentCaptor.forClass(List.class);
        listener = new TargetUploadFileReaderListener(
                consumer,
                9
        );
        EasyExcel.read(is, listener)
                .headRowNumber(1)
                .sheet()
                .doRead();

        verify(consumer, times(12)).accept(captor.capture());

        List<List<TargetRowResult>> captors = captor.getAllValues();

        assertThat(captors.get(11)).hasSize(1);
    }
}