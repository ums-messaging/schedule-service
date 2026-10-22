package com.ums.schedule.email.adaptor.out.persistence.jpa.entity;

import com.ums.schedule.email.domain.target.SendTargetError;
import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Table(name = "send_target_error")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SendTargetErrorEntity extends BaseEntity {
    @Id
    @Tsid
    private Long id;
    private Long targetId;
    private Long sendRequestId;
    private String errorType;
    @Column(name = "error_message", length = 2000)
    private String errorMessage;
    private Long groupId;
    private String payload;

    public static SendTargetErrorEntity fromDomain(SendTargetError domain) {
        SendTargetErrorEntity entity = new SendTargetErrorEntity(domain);
        return entity;
    }
    private SendTargetErrorEntity(SendTargetError domain) {
        this.id = domain.getId();
        this.groupId = domain.getGroupId();
        this.sendRequestId = domain.getSendRequestId();
        this.errorType = domain.getErrorType().name();
        this.errorMessage = domain.getErrorMessage();
        this.targetId = domain.getTargetId();
        this.payload = domain.getPayload();
    }
}
