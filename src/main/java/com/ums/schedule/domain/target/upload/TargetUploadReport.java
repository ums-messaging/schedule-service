package com.ums.schedule.domain.target.upload;

import com.github.f4b6a3.uuid.UuidCreator;
import com.ums.schedule.application.target.upload.model.TargetUploadReportCreateContext;
import com.ums.schedule.application.sendrequest.target.data.TargetMessageData;
import com.ums.schedule.application.sendrequest.target.command.TargetFileUploadRequestCommand;
import com.ums.schedule.application.sendrequest.target.result.SendTargetSaveResult;
import com.ums.schedule.common.code.api.TargetUploadErrorCode;
import com.ums.schedule.common.code.mapper.EnumMapperValue;
import com.ums.schedule.common.code.target_upload.TargetUploadEvent;
import com.ums.schedule.common.code.target_upload.TargetUploadFormatEnum;
import com.ums.schedule.common.code.target_upload.TargetUploadStatus;
import com.ums.schedule.common.code.target_upload.TargetUploadType;
import com.ums.schedule.common.util.FileUtil;
import com.ums.schedule.domain.request.SendRequest;

import com.ums.schedule.domain.request.converter.UuidBinaryConverter;
import com.ums.schedule.domain.target.SendTarget;
import com.ums.schedule.domain.target.converter.TargetUploadTypeConverter;
import com.ums.schedule.domain.target.upload.exception.TargetUploadPolicyViolationException;
import com.ums.schedule.domain.target.upload.state.TargetUploadCreateState;
import com.ums.schedule.domain.target.upload.state.TargetUploadState;
import com.ums.schedule.domain.target.upload.converter.TargetUploadReportStateConverter;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;


@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TargetUploadReport {
    @Id
    @Column(name = "report_id")
    @Convert(converter = UuidBinaryConverter.class)
    private UUID id;

    @Column(name = "upload_type", nullable = false)
    @Convert(converter = TargetUploadTypeConverter.class)
    private TargetUploadType uploadType;

    @Column(name = "upload_format")
    @Enumerated
    private TargetUploadFormatEnum uploadFormat;

    private Long totalCount;
    private Long successCount;
    private Long failCount;

    @Transient
    private TargetUploadEvent event;

    @Convert(converter = TargetUploadReportStateConverter.class)
    @Column(name = "status", nullable = false)
    private TargetUploadState state;

    @Column(name = "result_message")
    private String resultMessage;

    @Column(name = "file_size")
    private Long fileSize;

    @Column(name = "upload_key")
    private String uploadKey;

    @Column(name = "download_key")
    private String downloadKey;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "uploaded_at")
    private LocalDateTime uploadedAt;

    @Column(name = "requested_at")
    private LocalDateTime requestedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "request_id")
    private SendRequest sendRequest;

    public static TargetUploadReport of(TargetUploadReportCreateContext context) {
        TargetUploadReport targetUpload = new TargetUploadReport();
        String id = targetUpload.generateId();

        TargetUploadState state = targetUpload.initializeEventAndState();

        targetUpload.assignUploadType(context.uploadType());
        targetUpload.initializeSendRequestAndChangeState(state, context.sendRequest());
        targetUpload.initializeDownloadKey(context.downloadKeyPrefix(), id);
        targetUpload.initializeFileUploadTypeInfo(context, id);
        targetUpload.initializeCreatedAt();

        return targetUpload;
    }

    private void initializeFileUploadTypeInfo(TargetUploadReportCreateContext context, String id) {
        if(context.uploadType() == TargetUploadType.FILE) {
            TargetUploadFormatEnum format = initializeUploadFormat(context.uploadFormat());
            initializeUploadKey(format, context.uploadkeyPrefix(), id);
        }
    }

    private TargetUploadFormatEnum initializeUploadFormat(EnumMapperValue uploadFormat) {
        TargetUploadFormatEnum format = resolveUploadFormat(uploadFormat);
        assignUploadFormat(format);
        return format;
    }

    private void initializeUploadKey(TargetUploadFormatEnum format, String keyPrefix, String id) {
        Objects.requireNonNull(keyPrefix);
        this.uploadKey = FileUtil.generateFilePaths(keyPrefix, "%s.%s".formatted(id, format.value()));
    }

    private void assignUploadFormat(TargetUploadFormatEnum uploadFormat) {
        this.uploadFormat = uploadFormat;
    }

    private String generateId() {
        this.id = UuidCreator.getTimeOrdered();
        return this.id.toString();
    }

    public void initializeSendRequestAndChangeState(TargetUploadState state, SendRequest sendRequest) {
        this.state = state.onEvent(TargetUploadEvent.TARGET_UPLOAD_READY);
        assignSendRequest(sendRequest);
    }

    private void assignUploadType(TargetUploadType uploadType) {
        this.uploadType = Objects.requireNonNull(uploadType, "upload_type");
    }

    private TargetUploadFormatEnum resolveUploadFormat(EnumMapperValue uploadFormat) {
        this.uploadFormat = Optional.ofNullable(uploadFormat)
                .map(format -> TargetUploadFormatEnum.valueOf(format.code()))
                .orElseGet(() -> TargetUploadFormatEnum.CSV);
        return this.uploadFormat;
    }

    private TargetUploadState initializeEventAndState() {
        this.event = TargetUploadEvent.TARGET_UPLOAD_CREATED;
        return changeStatus(new TargetUploadCreateState());
    }

    private void initializeCreatedAt() {
        this.createdAt = LocalDateTime.now();
    }

    public void requestTargetUpload(Integer totalCount) {
        if(uploadType == TargetUploadType.JSON) {
            validateTotalCount(totalCount);
        }
        requestTargetUpload();
    }

    private void requestTargetUpload() {
        initializeRequestedAt();
        onEvent(TargetUploadEvent.TARGET_UPLOAD_REQUESTED);
    }

    private void validateTotalCount(Integer totalCount) {
        if(totalCount == 0) {
            throw TargetUploadPolicyViolationException.of(TargetUploadErrorCode.TARGET_LIST_OF_EMPTY);
        }
    }

    private void initializeRequestedAt() {
        this.requestedAt = LocalDateTime.now();
    }


    private void validateFileMetadata(TargetFileUploadRequestCommand command, String path) {
        validateFileSize(command.maxFileSize(), command.fileSize());
    }

    private void validateFileSize(Long maxFileSize, Long fileSize) {
        if(maxFileSize < fileSize) {
            throw TargetUploadPolicyViolationException.of(TargetUploadErrorCode.TARGET_UPLOAD_LIMIT_EXCEEDED, maxFileSize, fileSize);
        }
    }

    public void initializeDownloadKey(String fileKeyPrefix, String id) {
        String prefix = Objects.requireNonNull(fileKeyPrefix, "file_key_prefix");
        String filename = "%s.xlsx".formatted(id);
        this.downloadKey = FileUtil.generateFilePaths(prefix, filename);
    }

    public Map<Integer, List<SendTarget>> startTargetUploadAndGroupedTarget(List<SendTarget> targetList, int partitionSize) {
        Map<Integer, List<SendTarget>> groupedTargetList = targetList.stream()
                .collect(Collectors.groupingBy(i -> (targetList.indexOf(i) / partitionSize)));

        onEvent(TargetUploadEvent.TARGET_UPLOAD_STARTED);

        return groupedTargetList;
    }

    public void completeTargetUpload(List<SendTargetSaveResult> results) {
        long completedCount = results.stream()
                .mapToLong(target -> target.completedTargetList().size())
                .sum();

        long failedCount = results.stream()
                .mapToLong(target -> target.failedTargetList().size())
                .sum();

        if(isMismatchTotalCountBySum(completedCount, failedCount)) {
            throw TargetUploadPolicyViolationException.of(TargetUploadErrorCode.TARGET_UPLOAD_COUNT_MISMATCH);
        }

        this.successCount = completedCount;
        this.failCount = failedCount;

        onEvent(TargetUploadEvent.TARGET_UPLOAD_COMPLETED);
    }

    private boolean isMismatchTotalCountBySum(Long successCount, Long failCount) {
        Long sumCount = successCount + failCount;
        return totalCount != sumCount;
    }

    private TargetUploadState changeStatus(TargetUploadState state) {
        this.state = state;
        return state;
    }

    public boolean isCompleted() {
        return this.state.getCurrentCode() == TargetUploadStatus.COMPLETED;
    }

    private void onEvent(TargetUploadEvent event) {
        TargetUploadState toState = this.state.onEvent(event);
        changeStatus(toState);
        this.event = event;
    }

    public void onError(String message) {
        this.resultMessage = message;
        onEvent(TargetUploadEvent.TARGET_UPLOAD_FAIL);
    }

    private void assignSendRequest(SendRequest request) {
        this.sendRequest = Objects.requireNonNull(request, "send_request");
        request.assignTargetUpload(this);
    }
}