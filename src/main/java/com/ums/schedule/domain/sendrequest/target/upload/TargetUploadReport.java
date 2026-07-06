package com.ums.schedule.domain.sendrequest.target.upload;

import com.ums.schedule.application.sendrequest.command.TargetUploadCreateCommand;
import com.ums.schedule.application.sendrequest.target.data.TargetMessageData;
import com.ums.schedule.application.sendrequest.target.command.TargetFileUploadRequestCommand;
import com.ums.schedule.application.sendrequest.target.result.SendTargetSaveResult;
import com.ums.schedule.common.code.mapper.EnumMapperValue;
import com.ums.schedule.common.exception.validation.*;
import com.ums.schedule.common.util.FileUtil;
import com.ums.schedule.domain.sendrequest.SendRequest;

import com.ums.schedule.domain.sendrequest.code.ChannelTypeEnum;
import com.ums.schedule.domain.sendrequest.exception.SendRequestNotFoundException;
import com.ums.schedule.domain.sendrequest.target.SendTarget;
import com.ums.schedule.domain.sendrequest.target.converter.TargetUploadTypeConverter;
import com.ums.schedule.domain.sendrequest.target.exeption.SendTargetListExceedViolationException;
import com.ums.schedule.domain.sendrequest.target.exeption.SendTargetNotFoundException;
import com.ums.schedule.domain.sendrequest.target.upload.code.*;
import com.ums.schedule.domain.sendrequest.target.upload.exception.InvalidTargetTotalCountMismatchException;
import com.ums.schedule.domain.sendrequest.target.upload.exception.TargetUploadFileFormatMismatchException;
import com.ums.schedule.domain.sendrequest.target.upload.exception.UnsupportedTargetUploadTypeException;
import com.ums.schedule.domain.sendrequest.target.upload.exception.UploadKeyGeneratedViolationException;
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
import java.util.stream.Collectors;


@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class TargetUploadReport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uploadId;

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

    public static TargetUploadReport of(SendRequest sendRequest,  TargetUploadCreateCommand command, String filePrefix) {
        TargetUploadReport targetUpload = new TargetUploadReport();
        targetUpload.assignSendRequest(sendRequest);
        targetUpload.initializeUploadType(sendRequest.getChannelType(), command);
        targetUpload.initializeEventAndState();
        targetUpload.generateDownloadKey(sendRequest.getChannelType(), filePrefix);
        return targetUpload;
    }

    private void initializeUploadType(ChannelTypeEnum channelType, TargetUploadCreateCommand command) {
//        assignUploadType(command.uploadType());
//        if(this.uploadType == TargetUploadTypeEnum.JSON) {
//            initializeTotalCount(command.targetList(), command.targetListMaxSize());
//        } else {
//            resolveUploadFormat(command.uploadFormat());
//            generateUploadKey(channelType, command.filePrefix());
//        }
    }

    public void assignSendRequest(SendRequest sendRequest) {
        if(sendRequest == null) {
            throw SendRequestNotFoundException.of();
        }
        sendRequest.assignTargetUpload(this);
        this.sendRequest = sendRequest;
    }

    private void assignUploadType(EnumMapperValue uploadType) {
        if(uploadType == null) {
            throw RequiredException.fieldOf("upload_type");
        }
        this.uploadType = TargetUploadTypeEnum.valueOf(uploadType.code());
    }
    private void resolveUploadFormat(EnumMapperValue uploadFormat) {
        this.uploadFormat = Optional.ofNullable(uploadFormat)
                .map(format -> TargetUploadFormatEnum.valueOf(format.code()))
                .orElseGet(() -> TargetUploadFormatEnum.CSV);
    }

    private void initializeEventAndState() {
        this.event = TargetUploadEventEnum.TARGET_UPLOAD_CREATED;
        changeStatus(new TargetUploadCreateState());
        initializeCreatedAt();
    }

    private void initializeCreatedAt() {
        this.createdAt = LocalDateTime.now();
    }

    public boolean isReadyForUpload() {
        return this.state.getCurrentCode() == TargetUploadStatusEnum.WAITING;
    }

    private String generateUploadKey(ChannelTypeEnum channelType, String filePrefix) {
        if(isValidUploadKeyPrefix(filePrefix)) {
            throw UploadKeyGeneratedViolationException.of("file prefix is empty.");
        }
        String filename = String.format("%d.%s", uploadId, this.uploadFormat.value().toLowerCase());
        String uploadKey = generateObjectKeyPrefix(channelType, filePrefix) + filename;
        this.uploadKey = uploadKey;
        return uploadKey;
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
        assignSendRequest(sendRequest);
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
            throw UnsupportedTargetUploadTypeException.of();
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

    public String generateDownloadKey(ChannelTypeEnum channelType, String fileKeyPrefix) {
        String downloadKey = generateObjectKeyPrefix(channelType, fileKeyPrefix) + "download.xlsx";
        this.downloadKey = downloadKey;
        return downloadKey;
    }

    private String generateObjectKeyPrefix(ChannelTypeEnum channelType, String filePrefix) {
        return FileUtil.generateFilePaths(this.sendRequest.generateRequestUploadDir(channelType), filePrefix);
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

    private void changeStatus(TargetUploadState state) {
        this.state = state;
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
}