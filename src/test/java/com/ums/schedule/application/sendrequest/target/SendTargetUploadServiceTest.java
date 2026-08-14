package com.ums.schedule.application.sendrequest.target;

import com.ums.schedule.application.sendrequest.target.event.SendTargetFailedEvent;
import com.ums.schedule.application.sendrequest.target.result.SendTargetSaveResult;
import com.ums.schedule.application.ums.common.target.result.TargetMessageResult;
import com.ums.schedule.common.code.target.SendTargetRowStatus;
import com.ums.schedule.common.exception.BusinessException;
import com.ums.schedule.domain.target.SendTarget;
import com.ums.schedule.domain.target.state.SendTargetCompleteState;
import com.ums.schedule.domain.target.state.SendTargetFailState;
import com.ums.schedule.domain.target.upload.TargetUploadReport;
import com.ums.schedule.fixture.entity.SendTargetEntityBuilder;
import com.ums.schedule.fixture.target.TargetMessageResultBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
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

    @InjectMocks private SendTargetUploadService targetUploadService;

    private final List<TargetMessageResult> targetMessages = new ArrayList<>();
    private final List<SendTarget> targetList = new ArrayList<>();

    private TargetUploadReport targetUploadReport;

    @BeforeEach
    void setUp() {
        targetUploadReport = mock(TargetUploadReport.class);
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
                    .state(new SendTargetCompleteState())
                    .build();
            targetList.add(target);
        }
    }

    @Test
    @DisplayName("저장 성공 대상자는 성공 건수에 포함된다.")
    void shouldIncludePersistSucceedTargetsInSucceedCount() {
        doThrow(mock(BusinessException.class))
                .when(targetService).saveTargetList(any(), any());

        List<SendTarget> dbFailTargetList = new ArrayList<>();
        createFailTargets(dbFailTargetList, 15);
        createCompleteTargetList(dbFailTargetList, 5);

        doReturn(dbFailTargetList).when(targetService).saveTarget(any(), any());

        SendTargetSaveResult result = targetUploadService.upload(targetUploadReport, targetMessages);

        assertThat(result.completedTargetList())
                .hasSize(5);
    }

    @Test
    @DisplayName("저장 실패 대상자는 실패 건수에 포함된다.")
    void shouldIncludePersistFailedTargetsInFailedCount() {
        doThrow(mock(BusinessException.class))
                .when(targetService).saveTargetList(any(), any());

        List<SendTarget> dbFailTargetList = new ArrayList<>();
        createFailTargets(dbFailTargetList, 15);
        createCompleteTargetList(dbFailTargetList, 5);

        doReturn(dbFailTargetList).when(targetService).saveTarget(any(), any());

        SendTargetSaveResult result = targetUploadService.upload(targetUploadReport, targetMessages);

        assertThat(result.failedTargetList())
                .hasSize(15);
    }

    @Test
    @DisplayName("실패 대상자가 존재하지 않으면 이벤트를 발행하지 않는다.")
    void shouldNotPublishFailedTargetEvent_whenNoFailedTargetExists() {
        List<SendTarget> targetList = new ArrayList<>();
        createCompleteTargetList(targetList, 20);

        doReturn(targetList).when(targetService).saveTargetList(any(), any());

        targetUploadService.upload(targetUploadReport, targetMessages);

        verify(publisher, never()).publishEvent(any());
    }

    @Test
    @DisplayName("실패 대상자가 존재하면 이벤트를 발행한다.")
    void shouldPublishEvent_whenFailureSendTargetListExist() {
        doReturn(targetList).when(targetService).saveTargetList(any(), any());

        targetUploadService.upload(targetUploadReport, targetMessages);

        verify(publisher).publishEvent(any(SendTargetFailedEvent.class));
    }

    @Test
    @DisplayName("일괄 저장에 실패하면 개별 저장으로 재시도한다.")
    void shouldFallbackToSingleSave_whenBulkSaveFails() {
        List<SendTarget> targetList = new ArrayList<>();
        createCompleteTargetList(targetList, 10);
        createFailTargets(targetList, 10);

        doThrow(mock(BusinessException.class))
                .when(targetService).saveTargetList(any(), any());

        doAnswer(invocation -> {
            List<SendTarget> targets = invocation.getArgument(0);
            return SendTargetSaveResult.of(targets);
        }).when(targetService).saveTarget(any(), any());

        targetUploadService.upload(targetUploadReport, targetMessages);

        verify(targetService).saveTarget(any(), any(List.class));
    }
}