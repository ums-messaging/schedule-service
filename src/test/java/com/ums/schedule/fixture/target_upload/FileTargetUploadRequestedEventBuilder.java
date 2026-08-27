package com.ums.schedule.fixture.target_upload;

import com.ums.schedule.application.target.reader.model.FileTargetUploadRequestedEvent;
import com.ums.schedule.common.code.common.ChannelType;

import java.util.UUID;

public class FileTargetUploadRequestedEventBuilder {
    private ChannelType channelType;
    private Long requestId;
    private UUID messageId;
    private UUID uploadId;
    private String uploadKey;
    private String customerId;
    private Integer partitionSize;
    private Integer batchSize;

    public static FileTargetUploadRequestedEventBuilder builder() {
        return new FileTargetUploadRequestedEventBuilder();
    }

    private FileTargetUploadRequestedEventBuilder() {
        this.channelType = ChannelType.EMAIL;
        this.batchSize = 10000;
        this.requestId = 1L;
        this.uploadId = UUID.randomUUID();
        this.messageId = UUID.randomUUID();
        this.uploadKey = "target_upload.xlsx";
        this.partitionSize = 100;
    }

    public FileTargetUploadRequestedEventBuilder partitionSize(Integer partitionSize) {
        this.partitionSize = partitionSize;
        return this;
    }

    public FileTargetUploadRequestedEventBuilder channelType(ChannelType channelType) {
        this.channelType = channelType;
        return this;
    }

    public FileTargetUploadRequestedEventBuilder requestId(Long requestId) {
        this.requestId = requestId;
        return this;
    }

    public FileTargetUploadRequestedEventBuilder uploadId(UUID uuid) {
        this.uploadId = uuid;
        return this;
    }

    public FileTargetUploadRequestedEventBuilder messageId(UUID uuid) {
        this.messageId = messageId;
        return this;
    }

    public FileTargetUploadRequestedEventBuilder uploadKey(String uploadKey) {
        this.uploadKey = uploadKey;
        return this;
    }

    public FileTargetUploadRequestedEventBuilder batchSize(Integer batchSize) {
        this.batchSize = batchSize;
        return this;
    }

    public FileTargetUploadRequestedEvent build() {
        return new FileTargetUploadRequestedEvent(
                channelType,
                requestId,
                messageId,
                uploadId,
                uploadKey,
                customerId,
                partitionSize,
                batchSize
        );
    }

}
