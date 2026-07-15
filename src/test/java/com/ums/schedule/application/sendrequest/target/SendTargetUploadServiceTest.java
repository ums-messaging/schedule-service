package com.ums.schedule.application.sendrequest.target;

import com.ums.schedule.application.exception.template.TemplateLoadFailedException;
import com.ums.schedule.application.sendrequest.data.SendRequestKeyData;
import com.ums.schedule.application.sendrequest.target.assembler.SendTargetAssembler;
import com.ums.schedule.application.sendrequest.target.data.TargetMessageData;
import com.ums.schedule.application.sendrequest.target.assembler.EmailSendTargetAssembler;
import com.ums.schedule.application.sendrequest.target.event.SendTargetFailedEvent;
import com.ums.schedule.application.sendrequest.target.result.SendTargetSaveResult;
import com.ums.schedule.common.code.common.ChannelType;
import com.ums.schedule.domain.request.target.SendTarget;
import com.ums.schedule.domain.request.target.SendTargetTestBuilder;
import com.ums.schedule.domain.request.target.state.SendTargetFailState;
import com.ums.schedule.domain.request.target.state.SendTargetReadyState;
import com.ums.schedule.domain.request.target.upload.TargetUploadReport;
import com.ums.schedule.fixture.target_upload.TargetUploadReportEntityBuilder;
import com.ums.schedule.common.code.target_upload.TargetUploadStatusEnum;
import com.ums.schedule.domain.exception.target_upload.InvalidTargetTotalCountMismatchException;
import com.ums.schedule.domain.request.target.upload.state.TargetUploadRequestState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.dao.DataIntegrityViolationException;

import java.io.IOException;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SendTargetUploadServiceTest {
    private final Map<ChannelType, SendTargetAssembler> targetAssemblerMap = new HashMap<>();
    @Mock private ApplicationEventPublisher publisher;
    @Mock private EmailSendTargetAssembler targetAssembler;
    @Mock private SendTargetService targetService;

    private SendTargetUploadService targetUploadService;

    private final List<TargetMessageData> targetDataList = new ArrayList<>();
    private final List<SendTarget> targetList = new ArrayList<>();

    @BeforeEach
    void setUp() {
        targetAssemblerMap.put(ChannelType.EMAIL, targetAssembler);
        this.targetUploadService =
                new SendTargetUploadService(targetAssemblerMap, publisher, targetService);

        createFailTargets(targetList, 10);
        createCompleteTargetList(targetList, 10);
    }

    private void createFailTargets(List<SendTarget> targetList, int endIdx) {
        for(int i = 0; i < endIdx; i++) {
            SendTarget failureTarget = SendTargetTestBuilder.builder()
                            .state(new SendTargetFailState())
                                    .build();
            targetList.add(failureTarget);
        }
    }

    private void createCompleteTargetList(List<SendTarget> targetList, int endIdx) {
        for(int i = 0; i < endIdx; i++) {
            SendTarget target = SendTargetTestBuilder.builder()
                    .state(new SendTargetReadyState())
                    .build();
            targetList.add(target);
        }
    }

    @Test
    @DisplayName("조립 실패 대상자는 실패 건수에 포함된다.")
    void shouldIncludeAssemblerFailedTargetsInFailedCount() {
        TargetUploadReport report = TargetUploadReportEntityBuilder.builder()
                .uploadStatus(new TargetUploadRequestState())
                .totalCount(20L).build();
        SendRequestKeyData keyData = SendRequestKeyData.of(1L, UUID.randomUUID().toString(), ChannelType.EMAIL);

        SendTargetSaveResult givenResult = SendTargetSaveResult.of(targetList);

        doReturn(targetList).when(targetAssembler).assemble(any(), any(), any());
        doReturn(givenResult).when(targetService).saveTargetList(any());

        targetUploadService.upload(report, keyData, targetDataList, 20);

        assertThat(report.getFailCount()).isEqualTo(givenResult.failedTargetList().size());
    }

    @Test
    @DisplayName("저장 실패 대상자는 실패 건수에 포함된다.")
    void shouldIncludePersistFailedTargetsInFailedCount() {
        doReturn(targetList).when(targetAssembler).assemble(any(), any(), any());
        doThrow(new DataIntegrityViolationException("duplicated key"))
                .when(targetService).saveTargetList(any());

        List<SendTarget> dbFailTargetList = new ArrayList<>();
        createFailTargets(dbFailTargetList, 15);
        createCompleteTargetList(dbFailTargetList, 5);


        SendTargetSaveResult dbFailTargetResult = SendTargetSaveResult.of(dbFailTargetList);
        doReturn(dbFailTargetResult).when(targetService).saveTarget(any());

        TargetUploadReport report = TargetUploadReportEntityBuilder.builder()
                .uploadStatus(new TargetUploadRequestState())
                .totalCount(20L).build();

        SendRequestKeyData keyData = SendRequestKeyData.of(1L, UUID.randomUUID().toString(), ChannelType.EMAIL);

        targetUploadService.upload(report, keyData, targetDataList, 20);
    }

    @Test
    @DisplayName("실패 대상자가 존재하지 않으면 이벤트를 발행하지 않는다.")
    void shouldNotPublishFailedTargetEvent_whenNoFailedTargetExists() {
        TargetUploadReport report = TargetUploadReportEntityBuilder.builder()
                .uploadStatus(new TargetUploadRequestState())
                .totalCount(20L)
                .build();
        SendRequestKeyData keyData = SendRequestKeyData.of(1L, UUID.randomUUID().toString(), ChannelType.EMAIL);

        List<SendTarget> targetList = new ArrayList<>();
        createCompleteTargetList(targetList, 20);
        SendTargetSaveResult givenResult = SendTargetSaveResult.of(targetList);

        doReturn(targetList).when(targetAssembler).assemble(any(), any(), any());
        doReturn(givenResult).when(targetService).saveTargetList(any());

        targetUploadService.upload(report, keyData, targetDataList, 20);

        verify(publisher, never()).publishEvent(any());
    }

    @Test
    @DisplayName("실패 대상자가 존재하면 이벤트를 발행한다.")
    void shouldPublishEvent_whenFailureSendTargetListExist() {
        TargetUploadReport report = TargetUploadReportEntityBuilder.builder()
                .uploadStatus(new TargetUploadRequestState())
                .totalCount(20L)
                .build();
        SendRequestKeyData keyData = SendRequestKeyData.of(1L, UUID.randomUUID().toString(), ChannelType.EMAIL);

        SendTargetSaveResult givenResult = SendTargetSaveResult.of(targetList);

        doReturn(targetList).when(targetAssembler).assemble(any(), any(), any());
        doReturn(givenResult).when(targetService).saveTargetList(any());

        targetUploadService.upload(report, keyData, targetDataList, 20);

        verify(publisher).publishEvent(any(SendTargetFailedEvent.class));
    }

    @Test
    @DisplayName("대상자 업로드가 완료되면 상태는 COMPLETED가 된다.")
    void shouldChangeStateToCompleted_whenUploadCompletes() {
        TargetUploadReport report = TargetUploadReportEntityBuilder.builder()
                .uploadStatus(new TargetUploadRequestState())
                .totalCount(20L)
                .build();
        SendRequestKeyData keyData = SendRequestKeyData.of(1L, UUID.randomUUID().toString(), ChannelType.EMAIL);
        SendTargetSaveResult givenResult = SendTargetSaveResult.of(targetList);

        doReturn(targetList).when(targetAssembler).assemble(any(), any(), any());
        doReturn(givenResult).when(targetService).saveTargetList(any());

        targetUploadService.upload(report, keyData, targetDataList, 20);

        assertThat(report.getState().getCurrentCode()).isEqualTo(TargetUploadStatusEnum.COMPLETED);
    }

    @Test
    @DisplayName("대상자는 partitionSize 단위로 분할 처리된다.")
    void shouldPartitionTargetsByPartitionSize() {
        TargetUploadReport report = TargetUploadReportEntityBuilder.builder()
                .uploadStatus(new TargetUploadRequestState())
                .totalCount(20L)
                .build();

        SendRequestKeyData keyData = SendRequestKeyData.of(1L, UUID.randomUUID().toString(), ChannelType.EMAIL);

        List<SendTarget> targetList = new ArrayList<>();
        createCompleteTargetList(targetList, 20);

        ArgumentCaptor<List<SendTarget>> targetListCaptor = ArgumentCaptor.forClass(List.class);
        doReturn(targetList).when(targetAssembler).assemble(any(), any(), any());
        doAnswer(invocation -> {
            List<SendTarget> targets = invocation.getArgument(0);
            return SendTargetSaveResult.of(targets);
        }).when(targetService).saveTargetList(any());

        targetUploadService.upload(report, keyData, targetDataList, 5);

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
    @DisplayName("분할 처리된 대상자의 성공 및 실패 건수를 집계한다.")
    void shouldAggregateCompletedAndFailedCountsAcrossPartitions() {
        TargetUploadReport report = TargetUploadReportEntityBuilder.builder()
                .uploadStatus(new TargetUploadRequestState())
                .totalCount(20L)
                .build();

        SendRequestKeyData keyData = SendRequestKeyData.of(1L, UUID.randomUUID().toString(), ChannelType.EMAIL);

        List<SendTarget> targetList = new ArrayList<>();
        createCompleteTargetList(targetList, 5);
        createFailTargets(targetList, 15);

        doReturn(targetList).when(targetAssembler).assemble(any(), any(), any());
        doAnswer(invocation -> {
            List<SendTarget> targets = invocation.getArgument(0);
            return SendTargetSaveResult.of(targets);
        }).when(targetService).saveTargetList(any());

        targetUploadService.upload(report, keyData, targetDataList, 5);

        assertThat(report.getSuccessCount()).isEqualTo(5);
        assertThat(report.getFailCount()).isEqualTo(15);
    }

    @Test
    @DisplayName("대상자 조립 중 예외가 발생하면 상태는 ERROR가 된다.")
    void shouldChangeStateToError_whenAssemblerThrowsException() {
        TargetUploadReport report = TargetUploadReportEntityBuilder.builder()
                .uploadStatus(new TargetUploadRequestState())
                .totalCount(20L)
                .build();

        SendRequestKeyData keyData = SendRequestKeyData.of(1L, UUID.randomUUID().toString(), ChannelType.EMAIL);

        List<SendTarget> targetList = new ArrayList<>();
        createCompleteTargetList(targetList, 5);
        createFailTargets(targetList, 15);


        TemplateLoadFailedException exception = TemplateLoadFailedException.of(new IOException());
        doThrow(exception)
                .when(targetAssembler).assemble(any(), any(), any());

        targetUploadService.upload(report, keyData, targetDataList, 5);

        assertThat(report.getState().getCurrentCode()).isEqualTo(TargetUploadStatusEnum.FAIL);
        assertThat(report.getResultMessage()).isEqualTo(exception.getMessage());
    }

    @Test
    @DisplayName("전체 건수와 성공·실패 건수 합계가 다르면 상태는 ERROR로 변경된다.")
    void shouldChangeStateToError_whenTotalCountDoesNotMatchCompletedAndFailedCounts() {
        TargetUploadReport report = TargetUploadReportEntityBuilder.builder()
                .uploadStatus(new TargetUploadRequestState())
                .totalCount(20L)
                .build();

        SendRequestKeyData keyData = SendRequestKeyData.of(1L, UUID.randomUUID().toString(), ChannelType.EMAIL);

        List<SendTarget> targetList = new ArrayList<>();
        createCompleteTargetList(targetList, 5);
        createFailTargets(targetList, 10);

        doReturn(targetList).when(targetAssembler).assemble(any(), any(), any());
        doAnswer(invocation -> {
            List<SendTarget> targets = invocation.getArgument(0);
            return SendTargetSaveResult.of(targets);
        }).when(targetService).saveTargetList(any());

        targetUploadService.upload(report, keyData, targetDataList, 5);

        InvalidTargetTotalCountMismatchException expect = InvalidTargetTotalCountMismatchException.of(20L, 5L, 10L);

        assertThat(report.getState().getCurrentCode()).isEqualTo(TargetUploadStatusEnum.FAIL);
        assertThat(report.getResultMessage()).isEqualTo(expect.getMessage());
    }

    @Test
    @DisplayName("일괄 저장에 실패하면 개별 저장으로 재시도한다.")
    void shouldFallbackToSingleSave_whenBulkSaveFails() {
        TargetUploadReport report = TargetUploadReportEntityBuilder.builder()
                .uploadStatus(new TargetUploadRequestState())
                .totalCount(20L)
                .build();

        SendRequestKeyData keyData = SendRequestKeyData.of(1L, UUID.randomUUID().toString(), ChannelType.EMAIL);

        List<SendTarget> targetList = new ArrayList<>();
        createCompleteTargetList(targetList, 10);
        createFailTargets(targetList, 10);

        doReturn(targetList).when(targetAssembler).assemble(any(), any(), any());
        doThrow(new DataIntegrityViolationException("duplicated key"))
                .when(targetService).saveTargetList(any());

        doAnswer(invocation -> {
            List<SendTarget> targets = invocation.getArgument(0);
            return SendTargetSaveResult.of(targets);
        }).when(targetService).saveTarget(any());

        targetUploadService.upload(report, keyData, targetDataList, 5);

        verify(targetService, times(4)).saveTarget(any(List.class));
    }
}