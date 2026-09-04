package com.ums.schedule.application.target.upload;

import com.ums.schedule.application.sendrequest.target.SendTargetUploadService;
import com.ums.schedule.application.target.reader.model.TargetUploadRowResult;
import com.ums.schedule.application.target.uploader.EmailTargetUploadWorker;
import com.ums.schedule.application.target.uploader.model.EmailGeneratorContext;
import com.ums.schedule.application.ums.common.target.context.SendTargetGroupedList;
import com.ums.schedule.application.ums.email.generator.EmailTargetMessageGenerator;
import com.ums.schedule.domain.target.TargetMessage;
import com.ums.schedule.domain.target.message.EmailTargetMessage;
import com.ums.schedule.fixture.target.SendTargetGroupedListBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmailTargetUploadWorkerTest {
    @Mock private SendTargetUploadService targetUploadService;
    @Mock private EmailTargetMessageGenerator generator;
    @Mock private ThreadPoolTaskExecutor executor;

    private EmailTargetUploadWorker worker;

    private SendTargetGroupedListBuilder groupedListBuilder;

    @BeforeEach
    void setUp() {
        this.worker = new EmailTargetUploadWorker(
                generator,
                targetUploadService,
                executor
        );
        this.groupedListBuilder = SendTargetGroupedListBuilder.builder();
        doReturn(mock(EmailTargetMessage.class)).when(generator).generate(any(), any());
    }

    @Test
    @DisplayName("대상자 생성은 대상자 로우만큼 실행된다.")
    void shouldExecuteTargetCreateService() {
        TargetUploadRowResult row = mock(TargetUploadRowResult.class);
        List<TargetUploadRowResult> results = List.of(row, row, row);

        List<SendTargetGroupedList> groupedList = givenTargetList(results, 4);
        doAnswer(invocation -> {
            invocation.<Runnable>getArgument(0).run();
            return null;
        }).when(executor).execute(any());

        worker.process(mock(EmailGeneratorContext.class), groupedList);

        verify(generator, times(12)).generate(any(), any());

    }

    @Test
    @DisplayName("대상자 업로드는 파티션 개수 만큼 실행된다.")
    void shouldExecuteTargetUpload() {
        TargetUploadRowResult row = mock(TargetUploadRowResult.class);
        List<TargetUploadRowResult> results = List.of(row, row, row);

        List<SendTargetGroupedList> groupedList = givenTargetList(results, 4);

        doAnswer(invocation -> {
            invocation.<Runnable>getArgument(0).run();
            return null;
        }).when(executor).execute(any());

        worker.process(mock(EmailGeneratorContext.class), groupedList);

        verify(targetUploadService, times(4)).upload(any());
    }

    private List<SendTargetGroupedList> givenTargetList(List<TargetUploadRowResult> rows, int partitionSize) {
        List<SendTargetGroupedList> groupedList = new ArrayList<>();

        for(int i = 0; i<partitionSize; i++) {
            SendTargetGroupedList grouped = SendTargetGroupedListBuilder.builder()
                    .partitionNo(i)
                    .targetGroupedList(rows)
                    .build();
            groupedList.add(grouped);
        }
        return groupedList;
    }
}
