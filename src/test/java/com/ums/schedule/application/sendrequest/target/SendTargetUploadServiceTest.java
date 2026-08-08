package com.ums.schedule.application.sendrequest.target;

import com.ums.schedule.application.sendrequest.target.event.SendTargetFailedEvent;
import com.ums.schedule.application.sendrequest.target.result.SendTargetSaveResult;
import com.ums.schedule.domain.target.SendTarget;
import com.ums.schedule.fixture.entity.SendTargetEntityBuilder;
import com.ums.schedule.domain.target.state.SendTargetFailState;
import com.ums.schedule.domain.target.state.SendTargetReadyState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SendTargetUploadServiceTest {
    @Mock private ApplicationEventPublisher publisher;
    @Mock private SendTargetService targetService;

    private SendTargetUploadService targetUploadService;

    private final List<SendTarget> targetList = new ArrayList<>();

    @BeforeEach
    void setUp() {
        this.targetUploadService =
                new SendTargetUploadService(publisher, targetService);

        createFailTargets(targetList, 10);
        createCompleteTargetList(targetList, 10);
    }

    private void createFailTargets(List<SendTarget> targetList, int endIdx) {
        for(int i = 0; i < endIdx; i++) {
            SendTarget failureTarget = SendTargetEntityBuilder.builder()
                            .state(new SendTargetFailState())
                                    .build();
            targetList.add(failureTarget);
        }
    }

    private void createCompleteTargetList(List<SendTarget> targetList, int endIdx) {
        for(int i = 0; i < endIdx; i++) {
            SendTarget target = SendTargetEntityBuilder.builder()
                    .state(new SendTargetReadyState())
                    .build();
            targetList.add(target);
        }
    }

    @Test
    @DisplayName("조립 실패 대상자는 실패 건수에 포함된다.")
    void shouldIncludeAssemblerFailedTargetsInFailedCount() {
        SendTargetSaveResult givenResult = SendTargetSaveResult.of(targetList);

        doReturn(givenResult).when(targetService).saveTargetList(any());

        List<SendTargetSaveResult> result = targetUploadService.upload(targetList, 20);

        assertThat(result)
                .flatExtracting(SendTargetSaveResult::failedTargetList)
                .hasSize(10);

    }

    @Test
    @DisplayName("저장 실패 대상자는 실패 건수에 포함된다.")
    void shouldIncludePersistFailedTargetsInFailedCount() {
        doThrow(new DataIntegrityViolationException("duplicated key"))
                .when(targetService).saveTargetList(any());

        List<SendTarget> dbFailTargetList = new ArrayList<>();
        createFailTargets(dbFailTargetList, 15);
        createCompleteTargetList(dbFailTargetList, 5);

        SendTargetSaveResult dbFailTargetResult = SendTargetSaveResult.of(dbFailTargetList);
        doReturn(dbFailTargetResult).when(targetService).saveTarget(any());

        List<SendTargetSaveResult> results = targetUploadService.upload(targetList, 20);

        assertThat(results)
                .flatExtracting(SendTargetSaveResult::failedTargetList)
                .hasSize(15);
    }

    @Test
    @DisplayName("실패 대상자가 존재하지 않으면 이벤트를 발행하지 않는다.")
    void shouldNotPublishFailedTargetEvent_whenNoFailedTargetExists() {
        List<SendTarget> targetList = new ArrayList<>();
        createCompleteTargetList(targetList, 20);
        SendTargetSaveResult givenResult = SendTargetSaveResult.of(targetList);

        doReturn(givenResult).when(targetService).saveTargetList(any());

        targetUploadService.upload(targetList, 20);

        verify(publisher, never()).publishEvent(any());
    }

    @Test
    @DisplayName("실패 대상자가 존재하면 이벤트를 발행한다.")
    void shouldPublishEvent_whenFailureSendTargetListExist() {
        SendTargetSaveResult givenResult = SendTargetSaveResult.of(targetList);

        doReturn(givenResult).when(targetService).saveTargetList(any());

        targetUploadService.upload(targetList, 20);

        verify(publisher).publishEvent(any(SendTargetFailedEvent.class));
    }


    @Test
    @DisplayName("대상자는 partitionSize 단위로 분할 처리된다.")
    void shouldPartitionTargetsByPartitionSize() {
        List<SendTarget> targetList = new ArrayList<>();
        createCompleteTargetList(targetList, 20);

        ArgumentCaptor<List<SendTarget>> targetListCaptor = ArgumentCaptor.forClass(List.class);
        doAnswer(invocation -> {
            List<SendTarget> targets = invocation.getArgument(0);
            return SendTargetSaveResult.of(targets);
        }).when(targetService).saveTargetList(any());

        targetUploadService.upload(targetList, 5);

        verify(targetService, times(4))
                .saveTargetList(targetListCaptor.capture());
        assertThat(targetListCaptor.getAllValues())
                .extracting(List::size)
                .containsExactly(5, 5, 5, 5);
        assertThat(targetListCaptor.getAllValues())
                .flatExtracting(list -> list)
                .hasSize(20);
    }

    @Test
    @DisplayName("대상자 조립 중 예외가 발생하면 상태는 ERROR가 된다.")
    void shouldChangeStateToError_whenAssemblerThrowsException() {
        List<SendTarget> targetList = new ArrayList<>();
        createCompleteTargetList(targetList, 5);
        createFailTargets(targetList, 15);


//        TemplateLoadFailedException exception = TemplateLoadFailedException.of(new IOException());
//        doThrow(exception)
//                .when(targetAssembler).assemble(any(), any(), any());
//
//        targetUploadService.upload(report, keyData, targetDataList, 5);
//
//        assertThat(report.getState().getCurrentCode()).isEqualTo(TargetUploadStatusEnum.FAIL);
//        assertThat(report.getResultMessage()).isEqualTo(exception.getMessage());
    }

    @Test
    @DisplayName("일괄 저장에 실패하면 개별 저장으로 재시도한다.")
    void shouldFallbackToSingleSave_whenBulkSaveFails() {
        List<SendTarget> targetList = new ArrayList<>();
        createCompleteTargetList(targetList, 10);
        createFailTargets(targetList, 10);

        doThrow(new DataIntegrityViolationException("duplicated key"))
                .when(targetService).saveTargetList(any());

        doAnswer(invocation -> {
            List<SendTarget> targets = invocation.getArgument(0);
            return SendTargetSaveResult.of(targets);
        }).when(targetService).saveTarget(any());

        targetUploadService.upload(targetList, 5);

        verify(targetService, times(4)).saveTarget(any(List.class));
    }
}