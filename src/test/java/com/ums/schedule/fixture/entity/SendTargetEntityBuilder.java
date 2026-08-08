package com.ums.schedule.fixture.entity;

import com.ums.schedule.domain.target.SendTarget;
import com.ums.schedule.domain.target.TargetMessage;
import com.ums.schedule.domain.target.state.SendTargetCreateState;
import com.ums.schedule.domain.target.state.SendTargetState;
import com.ums.schedule.domain.target.upload.TargetUploadReport;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SendTargetEntityBuilder {
    private UUID id;
    private String targetKey = UUID.randomUUID().toString();
    private String targetName = "jang";
    private String contact="jang314@naver.com";
    private String messageVariable;
    private String resultMessage;
    private Integer attemptNo = 1;
    private TargetUploadReport targetUpload;
    private LocalDateTime createdAt;
    private LocalDateTime lastUploadedAt;
    private SendTargetState state;
    private Map<String, Object> dataParam = new HashMap<>();
    private TargetMessage targetMessage;

    public static SendTargetEntityBuilder builder() {
        return new SendTargetEntityBuilder();
    }

    private SendTargetEntityBuilder() {
        this.state = new SendTargetCreateState();
        this.createdAt = LocalDateTime.now();
    }

    public SendTargetEntityBuilder targetKey(String targetKey) {
        this.targetKey = targetKey;
        return this;
    }

    public SendTargetEntityBuilder targetName(String targetName) {
        this.targetName = targetName;
        return this;
    }

    public SendTargetEntityBuilder contact(String contact) {
        this.contact = contact;
        return this;
    }

    public SendTargetEntityBuilder messageVariable(String messageVariable) {
        this.messageVariable = messageVariable;
        return this;
    }

    public SendTargetEntityBuilder state(SendTargetState state) {
        this.state = state;
        return this;
    }

    public SendTargetEntityBuilder targetUpload(TargetUploadReport targetUpload) {
        this.targetUpload = targetUpload;
        return this;
    }

    public SendTargetEntityBuilder targetMessage(TargetMessage targetMessage) {
        this.targetMessage = targetMessage;
        return this;
    }
    public SendTargetEntityBuilder attemptNo(Integer attemptNo) {
        this.attemptNo = attemptNo;
        return this;
    }
    public SendTargetEntityBuilder createdAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public SendTarget build() {
        return new SendTarget(
                id,
                targetKey,
                targetName,
                contact,
                messageVariable,
                state,
                resultMessage,
                attemptNo,
                targetUpload,
                createdAt,
                lastUploadedAt,
                dataParam,
                targetMessage
                );
    }
}
