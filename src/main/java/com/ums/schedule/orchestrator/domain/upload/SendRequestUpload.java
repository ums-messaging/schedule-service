package com.ums.schedule.orchestrator.domain.upload;

import com.github.f4b6a3.tsid.TsidCreator;
import com.ums.schedule.common.code.target_upload.TargetUploadEvent;
import com.ums.schedule.orchestrator.application.upload.command.SendRequestUploadCreateCommand;
import com.ums.schedule.orchestrator.common.code.SendRequestUploadStatus;
import com.ums.schedule.orchestrator.common.code.UploadType;
import com.ums.schedule.orchestrator.adaptor.out.persistance.jpa.entity.SendRequestUploadEntity;
import com.ums.schedule.orchestrator.domain.common.ProvideUrl;
import com.ums.schedule.orchestrator.domain.upload.state.SendRequestUploadCreateState;
import com.ums.schedule.orchestrator.domain.upload.state.SendRequestUploadState;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SendRequestUpload {
    private Long id;
    private UploadType type;
    private String customerId;
    private String title;
    private String description;

    private SendRequestUploadState state;

    private Integer totalCount;

    private FileUploadMetadata fileMetadata;
    private ProvideUrl provideUrl;

    private LocalDateTime createdAt;
    private LocalDateTime providedAt;
    private LocalDateTime requestedAt;

    public static SendRequestUpload of(SendRequestUploadCreateCommand command) {
        return new SendRequestUpload(command);
    }

    private SendRequestUpload(SendRequestUploadCreateCommand command) {
        this.id = TsidCreator.getTsid().toLong();
        this.type = UploadType.valueOf(command.type().code());
        this.customerId = command.customerId();
        this.state = new SendRequestUploadCreateState();
        this.title = command.title();
        this.description = command.description();
    }

    public void provideUrl(ProvideUrl provideUrl) {
        this.provideUrl = provideUrl;
        this.providedAt = LocalDateTime.now();
        onEvent(TargetUploadEvent.PROVIDED_URL);
    }

    public void requestFileUpload(String objectKey, FileUploadMetadata fileMetadata) {
        this.provideUrl = ProvideUrl.of(objectKey);
        this.fileMetadata = fileMetadata;
        this.requestedAt = LocalDateTime.now();
        onEvent(TargetUploadEvent.FILE_UPLOAD_REQUEST);
    }

    private void onEvent(TargetUploadEvent event) {
        SendRequestUploadState toState = this.state.onEvent(event);
        changeState(toState);
    }

    private SendRequestUploadState changeState(SendRequestUploadState state) {
        this.state = state;
        return this.state;
    }

    public static SendRequestUpload fromEntity(SendRequestUploadEntity entity) {
        return new SendRequestUpload(entity);
    }

    private SendRequestUpload(SendRequestUploadEntity entity) {
        this.id = entity.getId();
        this.type = UploadType.valueOf(entity.getType());
        this.customerId = entity.getCustomerId();
        this.title = entity.getTitle();
        this.description = entity.getDescription();

        this.provideUrl = entity.toProviderUrl();
        this.fileMetadata = entity.toFileMetadata();
        this.totalCount = entity.getTotalCount();

        this.state = entity.toState();

        this.createdAt = entity.getCreatedAt();
        this.providedAt = entity.getProvidedAt();
        this.requestedAt = entity.getRequestedAt();
    }

    public SendRequestUploadEntity toEntity() {
        return SendRequestUploadEntity.fromDomain(this);
    }

    public String objectKey() {
        return existsProvideUrl() ? provideUrl.objectKey() : null;
    }
    public String provideUrl() {
        return existsProvideUrl() ? provideUrl.provideUrl() : null;
    }
    public Long fileSize() {
        return existsFileMetadata() ? fileMetadata.fileSize() : 0L;
    }

    public String contentType() {
        return existsFileMetadata() ? fileMetadata.contentType().code() : null;
    }

    private boolean existsProvideUrl() {
        return this.provideUrl != null;
    }
    private boolean existsFileMetadata() {
        return this.fileMetadata != null;
    }
    public LocalDateTime expiredAt() {
        if(existsProvideUrl() && currentStateIsWaiting()) {
            if(LocalDateTime.now().isBefore(provideUrl.expiredAt())) {
                return provideUrl.expiredAt();
            }
        }
        return null;
    }

    private boolean currentStateIsWaiting() {
        return this.state.getCurrentCode() == SendRequestUploadStatus.WAITING;
    }
}
