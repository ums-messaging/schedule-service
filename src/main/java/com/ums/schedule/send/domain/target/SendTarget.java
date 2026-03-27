package com.ums.schedule.send.domain.target;

import com.ums.schedule.send.application.model.command.SendTargetCreateCommand;
import com.ums.schedule.send.application.model.dto.SendTargetDto;
import com.ums.schedule.send.code.SendTargetStatusEnum;
import com.ums.schedule.send.domain.request.SendRequest;
import com.ums.schedule.send.domain.target.upload.TargetUpload;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
public class SendTarget {
    private String id;
    private String targetKey;
    private String targetName;
    private TargetAddress address;
    private String messageVariable;
    private SendTargetStatusEnum status; // ready, retrying, success, fail, sending,
    private TargetError targetError;
    private Integer attemptNo;

    private SendRequest sendRequest;
    private TargetUpload targetUpload;

    private LocalDateTime createdAt;
    private LocalDateTime lastUploadedAt;

    public static SendTarget of(SendTargetDto dto, TargetAddress address) {
        SendTarget sendTarget = new SendTarget(dto);
        sendTarget.parseToJson(dto.messageVariable());
        sendTarget.applyTargetContact(address);
        return sendTarget;
    }

    private void applyTargetContact(TargetAddress address) {
        this.address = address;
    }

    private void parseToJson(Map<String, Object> messageVariable) {
    }

    private SendTarget(SendTargetDto dto) {
        this.targetKey = dto.targetKey();
        this.targetName = dto.targetName();
        this.attemptNo = 1;
    }

    public SendTarget applySendRequest(SendRequest sendRequest) {
        sendRequest.getTargetList().add(this);
        this.sendRequest = sendRequest;
        return this;
    }



    public void toError(TargetError targetError) {
        this.targetError = targetError;
    }
    // 채널 타입에 따라 contact 유효성 검증
    // template 참고해서 emssageVariable 검증
}
