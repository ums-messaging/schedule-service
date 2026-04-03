package com.ums.schedule.send.domain.target;

import com.ums.schedule.send.application.model.dto.SendTargetDto;
import com.ums.schedule.send.code.SendTargetStatusEnum;
import com.ums.schedule.send.code.TargetColumnEnum;
import com.ums.schedule.send.domain.request.SendRequest;
import com.ums.schedule.send.domain.target.status.SendTargetReadyStatus;
import com.ums.schedule.send.domain.target.status.SendTargetStatus;
import jakarta.persistence.Entity;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Map;

import static com.ums.schedule.send.code.TargetColumnEnum.TARGET_KEY;
import static com.ums.schedule.send.code.TargetColumnEnum.TARGET_NAME;

@Getter
@Entity
public class SendTarget {
    private String id;
    private String targetKey;
    private String targetName;
    private TargetAddress address;
    private String messageVariable;
    private SendTargetStatus state;
    private SendTargetStatusEnum status; // ready, retrying, success, fail, sending,
    private Integer attemptNo;

    private SendRequest sendRequest;

    private LocalDateTime createdAt;
    private LocalDateTime lastUploadedAt;

    public static SendTarget of(SendTargetDto dto, TargetAddress address) {
        SendTarget sendTarget = new SendTarget(dto.targetData());
        sendTarget.applyTargetContact(address);
        Map<String, Object> messageVariableMap = dto.extractMessageVariable();

        return sendTarget;
    }

    private void changeTargetStatus(SendTargetStatus state) {
        this.state = state;
        this.status = state.currentStatusCode();
    }

    private void applyTargetContact(TargetAddress address) {
        this.address = address;
    }

    private SendTarget(Map<Long, String> targetMap) {
        this.targetKey = targetMap.get(TARGET_KEY);
        this.targetName = targetMap.get(TARGET_NAME);
        this.createdAt = LocalDateTime.now();
        this.attemptNo = 1;
    }

    public SendTarget applySendRequest(SendRequest sendRequest) {
        sendRequest.getTargetList().add(this);
        this.sendRequest = sendRequest;
        return this;
    }
}
