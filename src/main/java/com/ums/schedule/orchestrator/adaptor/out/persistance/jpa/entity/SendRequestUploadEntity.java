package com.ums.schedule.orchestrator.adaptor.out.persistance.jpa.entity;



import com.ums.schedule.common.code.common.FileContentType;
import com.ums.schedule.orchestrator.common.code.SendRequestUploadStatus;
import com.ums.schedule.orchestrator.domain.upload.FileUploadMetadata;
import com.ums.schedule.orchestrator.domain.common.ProvideUrl;
import com.ums.schedule.orchestrator.domain.upload.SendRequestUpload;
import com.ums.schedule.orchestrator.domain.upload.state.SendRequestUploadState;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Persistable;
import java.time.LocalDateTime;
import java.util.Optional;


@Getter
@Entity
@Table(name = "send_request_upload",
        uniqueConstraints = {
                @UniqueConstraint(
                        name="uq_customer_object_key",
                        columnNames = {"customer_id", "object_key"}
                )}
)
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SendRequestUploadEntity implements Persistable<Long> {
    @Id
    @Column(name = "id")
    private Long id;
    @Column(name = "type", nullable = false)
    private String type;
    @Column(name = "customer_id", nullable = false)
    private String customerId;

    @Column(name = "title", nullable = false)
    private String title;
    @Column(name = "description", length = 2000)
    private String description;

    @Column(name = "content_type")
    private String contentType;

    @Column(name = "file_size")
    private Long fileSize;
    @Column(name = "object_key", length = 1000)
    private String objectKey;
    @Column(name = "provide_url", length = 2000)
    private String provideUrl;
    @Column(name = "expired_at")
    private LocalDateTime expiredAt;

    @Column(name = "total_count")
    private Integer totalCount;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "provided_at")
    private LocalDateTime providedAt;

    @Column(name = "requested_at")
    private LocalDateTime requestedAt;

    @Transient
    private boolean isNew;

    public static SendRequestUploadEntity fromDomain(SendRequestUpload domain) {
        return new SendRequestUploadEntity(domain);
    }

    private SendRequestUploadEntity(SendRequestUpload domain) {
        this.id = domain.getId();
        this.type = domain.getType().code();
        this.customerId = domain.getCustomerId();
        this.title = domain.getTitle();
        this.description = domain.getDescription();
        this.objectKey = domain.objectKey();
        this.contentType = domain.contentType();
        this.provideUrl = domain.provideUrl();
        this.fileSize = domain.fileSize();
        this.status = domain.getState().getCurrentCode().code();
        this.createdAt = domain.getCreatedAt();
        this.providedAt = domain.getProvidedAt();
        this.requestedAt = domain.getRequestedAt();
        this.expiredAt = domain.expiredAt();
    }

    public SendRequestUpload toDomain() {
        return SendRequestUpload.fromEntity(this);
    }

    @Override
    public boolean isNew() {
        return this.isNew;
    }

    @PrePersist
    public void prePersist() {
        if(this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
            this.isNew = true;
        }
    }

    @PostLoad
    public void postLoad() {
        this.isNew = false;
    }

    public ProvideUrl toProviderUrl() {
        return ProvideUrl.of(objectKey, provideUrl, expiredAt);
    }

    public FileUploadMetadata toFileMetadata(){
        FileContentType contentType = Optional.ofNullable(this.contentType)
                .map(FileContentType::valueOf)
                .orElse(null);
        return new FileUploadMetadata(contentType, fileSize);
    }

    public SendRequestUploadState toState() {
        SendRequestUploadStatus status = SendRequestUploadStatus.valueOf(this.status);
        return status.createStatus();
    }
}