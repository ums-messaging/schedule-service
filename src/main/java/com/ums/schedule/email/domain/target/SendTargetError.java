package com.ums.schedule.email.domain.target;

import com.ums.schedule.email.adaptor.out.persistence.jpa.entity.SendTargetErrorEntity;
import com.ums.schedule.email.common.code.SendTargetErrorTypeCode;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SendTargetError {
    private Long id;
    private Long targetId;
    private Long sendRequestId;
    private SendTargetErrorTypeCode errorType;
    private String errorMessage;
    private Long groupId;
    private String payload;
    private LocalDateTime createdAt;

    public static SendTargetError of(SendTargetRequest target, SendTargetErrorTypeCode errorType, String errorMessage) {
        SendTargetError error = new SendTargetError(target);
        error.assignTargetId(target.getId(), String.valueOf(target.getMessageVariable().get("row_no")));
        error.assignErrorData(errorType, errorMessage);
        return error;
    }

    private void assignTargetId(Long targetId, String rowNo) {
        Long no = Optional.ofNullable(rowNo)
                .map(Long::parseLong)
                .orElse(0L);
        this.targetId = Optional.ofNullable(targetId)
                .orElse(no);

    }

    private void assignErrorData(SendTargetErrorTypeCode errorType, String errorMessage) {
        this.errorType = errorType;
        this.errorMessage = errorMessage;
    }
    private SendTargetError(SendTargetRequest target) {
        this.sendRequestId = target.getSendRequestId();
        this.groupId = Optional.ofNullable(target.getGroup().groupId())
                .orElse(0L);
        this.payload = target.getMessageVariable().toString();
        this.createdAt = LocalDateTime.now();
    }
    public SendTargetErrorEntity toEntity() {
        return SendTargetErrorEntity.fromDomain(this);
    }
}
