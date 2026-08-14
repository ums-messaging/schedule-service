package com.ums.schedule.application.target.reader;

import com.alibaba.excel.EasyExcel;
import com.ums.schedule.application.target.reader.model.TargetRowResult;
import com.ums.schedule.application.ums.common.target.context.SendTargetGroupedListContext;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TargetUploadFileReaderListenerTest {
    @Mock private Consumer<List<SendTargetGroupedListContext>> consumer;
    private TargetUploadFileReaderListener listener;

    @TempDir
    private Path temp;
    private InputStream is;

    @BeforeEach
    void setUp() throws IOException {
        temp = TargetUploadCreateFileUtils.createTargetList(1000);
        is = Files.newInputStream(temp);

    }

    @Nested
    class WhenReadSuccess {
        @BeforeEach
        void setUp() {
            listener = new TargetUploadFileReaderListener(
                    consumer,
                    10,
                    100
            );

            EasyExcel.read(is, listener)
                    .headRowNumber(1)
                    .sheet()
                    .doRead();
        }
        @Test
        @DisplayName("대상자 파싱에 성공하면 SUCCESS 상태를 저장한다.")
        void shouldAddSuccessTargetRowResult_whenParsingSucceeds() {
            ArgumentCaptor<List<SendTargetGroupedListContext>> captor = ArgumentCaptor.forClass(List.class);

            verify(consumer, times(10)).accept(captor.capture());

            List<List<SendTargetGroupedListContext>> allValues = captor.getAllValues();

            assertThat(allValues)
                    .flatExtracting(batch -> batch)
                    .extracting(SendTargetGroupedListContext::targetGroupedList)
                    .flatExtracting(list -> list)
                    .extracting(TargetRowResult::status)
                            .contains(SendTargetRowStatus.SUCCESS);
        }
        @Test
        @DisplayName("대상자 목록이 배치 사이즈에 도달하면 업로더를 호출한다.")
        void shouldInvokeConsumer_whenBatchSizeReached() {
            verify(consumer, times(10)).accept(any());
        }

        @Test
        @DisplayName("Consumer에 배치 크기만큼의 대상자를 전달한다.")
        void shouldPassBatchSizeTargetsToConsumer() {
            ArgumentCaptor<List<SendTargetGroupedListContext>> captor = ArgumentCaptor.forClass(List.class);

            verify(consumer, times(10)).accept(captor.capture());

            List<List<SendTargetGroupedListContext>> captors = captor.getAllValues();

            assertThat(captors)
                    .allSatisfy(batch -> assertThat(batch).hasSize(10));
        }
        @Test
        @DisplayName("대상자는 batchSize 만큼 생성된다.")
        void shouldCreateTargetListByBatchSize() {
            ArgumentCaptor<List<SendTargetGroupedListContext>> captor = ArgumentCaptor.forClass(List.class);

            verify(consumer, times(10)).accept(captor.capture());
            List<List<SendTargetGroupedListContext>> allValues = captor.getAllValues();

            assertThat(allValues)
                    .flatExtracting(batch -> batch)
                    .hasSize(100);
        }

        @Test
        @DisplayName("대상자는 partitionSize 단위로 분할 처리된다.")
        void shouldPartitionTargetsByPartitionSize() {
            ArgumentCaptor<List<SendTargetGroupedListContext>> captor = ArgumentCaptor.forClass(List.class);

            verify(consumer, times(10)).accept(captor.capture());

            List<List<SendTargetGroupedListContext>> allValues = captor.getAllValues();

            assertThat(allValues)
                    .flatExtracting(batch -> batch)
                    .extracting(SendTargetGroupedListContext::targetGroupedList)
                    .extracting(List::size)
                    .isNotEmpty()
                    .containsOnly(10);
        }
    }

    @Nested
    @DisplayName("남은 데이터 처리")
    class WhenDoAfterAllAnalysed {
        @Test
        @DisplayName("분석 종료 후 남은 대상자를 Consumer에 전달한다.")
        void shouldInvokeConsumerWithRemainingTargets_whenRemainingTargetsExist() {
            ArgumentCaptor<List<SendTargetGroupedListContext>> captor = ArgumentCaptor.forClass(List.class);
            listener = new TargetUploadFileReaderListener(
                    consumer,
                    10,
                    99
            );
            EasyExcel.read(is, listener)
                    .headRowNumber(1)
                    .sheet()
                    .doRead();

            verify(consumer, times(11)).accept(captor.capture());

            List<List<SendTargetGroupedListContext>> captors = captor.getAllValues();

            assertThat(captors.get(10)).hasSize(1);
    }


    }

}