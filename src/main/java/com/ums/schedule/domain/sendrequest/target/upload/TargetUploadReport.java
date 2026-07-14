package com.ums.schedule.domain.sendrequest.target.upload;

import com.github.f4b6a3.uuid.UuidCreator;
import com.ums.schedule.application.sendrequest.context.TargetUploadReportCreateContext;
import com.ums.schedule.application.sendrequest.target.data.TargetMessageData;
import com.ums.schedule.application.sendrequest.target.command.TargetFileUploadRequestCommand;
import com.ums.schedule.application.sendrequest.target.result.SendTargetSaveResult;
import com.ums.schedule.common.code.mapper.EnumMapperValue;
import com.ums.schedule.common.code.target_upload.TargetUploadEventEnum;
import com.ums.schedule.common.code.target_upload.TargetUploadFormatEnum;
import com.ums.schedule.common.code.target_upload.TargetUploadStatusEnum;
import com.ums.schedule.common.code.target_upload.TargetUploadTypeEnum;
import com.ums.schedule.common.exception.validation.*;
import com.ums.schedule.common.util.FileUtil;
import com.ums.schedule.domain.exception.target_upload.*;
import com.ums.schedule.domain.sendrequest.SendRequest;

import com.ums.schedule.domain.sendrequest.converter.UuidBinaryConverter;
import com.ums.schedule.domain.exception.request.SendRequestNotFoundException;
import com.ums.schedule.domain.sendrequest.target.SendTarget;
import com.ums.schedule.domain.sendrequest.target.converter.TargetUploadTypeConverter;
import com.ums.schedule.domain.exception.target.SendTargetListExceedViolationException;
import com.ums.schedule.domain.exception.target.SendTargetNotFoundException;
import com.ums.schedule.domain.sendrequest.target.upload.state.TargetUploadCreateState;
import com.ums.schedule.domain.sendrequest.target.upload.state.TargetUploadState;
import com.ums.schedule.domain.sendrequest.target.upload.converter.TargetUploadReportStateConverter;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
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
    private TargetUploadTypeEnum uploadType;

    @Column(name = "upload_format")
    @Enumerated
    private TargetUploadFormatEnum uploadFormat;

    private Long totalCount;
    private Long successCount;
    private Long failCount;

    @Transient
    private TargetUploadEventEnum event;

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
        TargetUploadState state = targetUpload.initializeEventAndState();
        String id = targetUpload.generateId();

        targetUpload.assignUploadType(context.uploadType());
        targetUpload.initializeSendRequestAndChangeState(state, context.sendRequest());
        targetUpload.initializeDownloadKey(context.sendRequest(), context.downloadKeyPrefix(), id);
        targetUpload.initializeFileUploadTypeInfo(context, id);
        targetUpload.initializeCreatedAt();

        return targetUpload;
    }

    private void initializeFileUploadTypeInfo(TargetUploadReportCreateContext context, String id) {
        if(context.uploadType() == TargetUploadTypeEnum.FILE) {
            TargetUploadFormatEnum format = initializeUploadFormat(context.uploadFormat());
            initializeUploadKey(context.sendRequest(), format, context.uploadkeyPrefix(), id);
        }
    }

    private TargetUploadFormatEnum initializeUploadFormat(EnumMapperValue uploadFormat) {
        TargetUploadFormatEnum format = resolveUploadFormat(uploadFormat);
        assignUploadFormat(format);
        return format;
    }

    private void initializeUploadKey(SendRequest sendRequest, TargetUploadFormatEnum format, String keyPrefix, String id) {
        String fileName = "%s.%s".formatted(id, format.code().toLowerCase());
        this.uploadKey = generateUploadKey(sendRequest, keyPrefix, fileName);
    }
    private String generateUploadKey(SendRequest sendRequest, String filePrefix, String fileName) {
        String baseDir = sendRequest.generateRequestUploadDir();
        return Optional.ofNullable(filePrefix)
                .filter(StringUtils::hasText)
                .map(prefix -> FileUtil.generateFilePaths(filePrefix, baseDir, fileName))
                .orElseThrow(TargetUploadKeyGenerationFailedException::of);
    }
    private void assignUploadFormat(TargetUploadFormatEnum uploadFormat) {
        this.uploadFormat = uploadFormat;
    }

    private TargetUploadFormatEnum resolveUploadFormat(TargetUploadFormatEnum uploadFormat) {
        return Optional.ofNullable(uploadFormat)
                .orElse(TargetUploadFormatEnum.CSV);
    }

    private String generateId() {
        this.id = UuidCreator.getTimeOrdered();
        return this.id.toString();
    }

    public void initializeSendRequestAndChangeState(TargetUploadState state, SendRequest sendRequest) {
        this.state = state.onEvent(TargetUploadEventEnum.TARGET_UPLOAD_READY);
        this.sendRequest = Optional.ofNullable(sendRequest)
                        .map(this::assignSendRequest)
                .orElseThrow(SendRequestNotFoundException::of);
    }

    private void assignUploadType(TargetUploadTypeEnum uploadType) {
        this.uploadType = Optional.ofNullable(uploadType)
                .orElseThrow(TargetUploadTypeNotFoundException::of);
    }

    private TargetUploadFormatEnum resolveUploadFormat(EnumMapperValue uploadFormat) {
        this.uploadFormat = Optional.ofNullable(uploadFormat)
                .map(format -> TargetUploadFormatEnum.valueOf(format.code()))
                .orElseGet(() -> TargetUploadFormatEnum.CSV);
        return this.uploadFormat;
    }

    private TargetUploadState initializeEventAndState() {
        this.event = TargetUploadEventEnum.TARGET_UPLOAD_CREATED;
        return changeStatus(new TargetUploadCreateState());
    }

    private void initializeCreatedAt() {
        this.createdAt = LocalDateTime.now();
    }

    public boolean isReadyForUpload() {
        return this.state.getCurrentCode() == TargetUploadStatusEnum.WAITING;
    }



    private boolean isValidUploadKeyPrefix(String filePrefix) {
        return uploadType == TargetUploadTypeEnum.FILE && !StringUtils.hasText(filePrefix);
    }
    private void initializeTotalCount(List<TargetMessageData> targetList, Integer maxSize) {
        Long totalCount = countingTargetList(targetList, maxSize);
        this.totalCount = totalCount;
    }

    private void validateTotalCount(Integer totalCount, Integer maxSize) {
        if(totalCount == 0) {
            throw SendTargetNotFoundException.listOf(this.sendRequest.getId());
        } else if(totalCount > maxSize){
            throw SendTargetListExceedViolationException.of(maxSize);
        }
    }
    private Long countingTargetList(List<TargetMessageData> targetList, Integer maxSize) {
        validateTotalCount(targetList.size(), maxSize);
        return (long) targetList.size();
    }
    public void prepareTargetUpload(SendRequest sendRequest) {
        onEvent(TargetUploadEventEnum.TARGET_UPLOAD_READY);
        if(this.uploadType == TargetUploadTypeEnum.JSON) {
            requestTargetUpload();
        }
        sendRequest.updateStateByTargetUploadReport(this);
    }

    private void requestTargetUpload() {
        initializeRequestedAt();
        onEvent(TargetUploadEventEnum.TARGET_UPLOAD_REQUESTED);
    }

    private void initializeRequestedAt() {
        this.requestedAt = LocalDateTime.now();
    }

    private void initializeFileMetaData(TargetFileUploadRequestCommand command) {
        validateFileMetadata(command, this.uploadKey);
        this.fileSize = command.fileSize();
    }

    public void requestFileUpload(TargetFileUploadRequestCommand command) {
        if(uploadType == TargetUploadTypeEnum.JSON) {
            throw UnSupportedTargetUploadTypeException.of();
        }
        initializeFileMetaData(command);
        requestTargetUpload();
    }

    private void validateFileMetadata(TargetFileUploadRequestCommand command, String path) {
        validateFileExists(command.isExistFile(), path);
        validateFileSize(command.maxFileSize(), command.fileSize());
        validateFileName(command.fileName());
    }

    private void validateFileName(String fileName) {
        if(StringUtils.hasText(fileName)) {
            String[] extractFileExt = fileName.split("\\.");
            if(extractFileExt.length < 2) {
                throw InvalidFilenameValueException.of();
            }
            validateFileExtension(extractFileExt);
            return;
        }
        throw RequiredException.fieldOf("upload filename");
    }

    private void validateFileExtension(String[] extractFileExt) {
       if(!extractFileExt[1].equals(this.uploadFormat.value())) {
            throw TargetUploadFileFormatMismatchException.of(this.uploadFormat);
        }
    }

    private void validateFileExists(boolean existFile, String path) {
        if(!existFile) {
            throw FileNotFoundException.of(path);
        }
    }

    private void validateFileSize(Long maxFileSize, Long fileSize) {
        if(maxFileSize < fileSize) {
            throw FileSizeExceededException.of(maxFileSize, fileSize);
        }
    }

    public void initializeDownloadKey(SendRequest sendRequest, String fileKeyPrefix, String id) {
        this.downloadKey = Optional.ofNullable(fileKeyPrefix)
                .filter(StringUtils::hasText)
                .map(str -> generateDownloadKey(sendRequest, fileKeyPrefix, id))
                .orElseThrow(TargetDownloadKeyGenerationFailedException::of);
    }

    private String generateDownloadKey(SendRequest sendRequest, String fileKeyPrefix, String id) {
        String baseDir = sendRequest.generateRequestUploadDir();
        return FileUtil.generateFilePaths(fileKeyPrefix, baseDir, "%s.xlsx".formatted(id));
    }

    public Map<Integer, List<SendTarget>> startTargetUploadAndGroupedTarget(List<SendTarget> targetList, int partitionSize) {
        Map<Integer, List<SendTarget>> groupedTargetList = targetList.stream()
                .collect(Collectors.groupingBy(i -> (targetList.indexOf(i) / partitionSize)));

        onEvent(TargetUploadEventEnum.TARGET_UPLOAD_STARTED);

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
            throw InvalidTargetTotalCountMismatchException.of(totalCount, completedCount, failedCount);
        }

        this.successCount = completedCount;
        this.failCount = failedCount;

        onEvent(TargetUploadEventEnum.TARGET_UPLOAD_COMPLETED);
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
        return this.state.getCurrentCode() == TargetUploadStatusEnum.COMPLETED;
    }

    private void onEvent(TargetUploadEventEnum event) {
        TargetUploadState toState = this.state.onEvent(event);
        changeStatus(toState);
        this.event = event;
    }

    public void onError(String message) {
        this.resultMessage = message;
        onEvent(TargetUploadEventEnum.TARGET_UPLOAD_FAIL);
    }

    private SendRequest assignSendRequest(SendRequest request) {
        request.assignTargetUpload(this);
        return request;
    }
}