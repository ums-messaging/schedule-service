package com.ums.schedule.domain.sendrequest.target;

import com.ums.schedule.domain.sendrequest.target.code.SendTargetStatusEnum;
import com.ums.schedule.domain.sendrequest.target.state.SendTargetState;
import com.ums.schedule.domain.sendrequest.target.upload.TargetUploadReport;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class SendTargetTestBuilder {
    private String targetKey = UUID.randomUUID().toString();
    private String targetName = "jang";
    private String contact="jang314@naver.com";
    private String resourceJson;
    private String messageVariable;
    private String resultMessage;
    private SendTargetStatusEnum status = SendTargetStatusEnum.READY;
    private Integer attemptNo = 1;
    private String title = "title";
    private String content = "content";
    private TargetUploadReport targetUpload;
    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime lastUploadedAt;
    private SendTargetState state;
    private Map<String, Object> dataParam = new HashMap<>();

    public static SendTargetTestBuilder builder() {
        return new SendTargetTestBuilder();
    }

    public SendTargetTestBuilder targetKey(String targetKey) {
        this.targetKey = targetKey;
        return this;
    }

    public SendTargetTestBuilder targetName(String targetName) {
        this.targetName = targetName;
        return this;
    }
    public SendTargetTestBuilder content(String content) {
        this.content = content;
        return this;
    }

    public SendTargetTestBuilder contact(String contact) {
        this.contact = contact;
        return this;
    }

    public SendTargetTestBuilder messageVariable(String messageVariable) {
        this.messageVariable = messageVariable;
        return this;
    }

    public SendTargetTestBuilder state(SendTargetState state) {
        this.state = state;
        this.status = (state == null) ? null : state.currentStatusCode();
        return this;
    }

    public SendTargetTestBuilder targetUpload(TargetUploadReport targetUpload) {
        this.targetUpload = targetUpload;
        return this;
    }
    public SendTargetTestBuilder attemptNo(Integer attemptNo) {
        this.attemptNo = attemptNo;
        return this;
    }
    public SendTargetTestBuilder createdAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }
    public SendTarget build() {
        return new SendTarget(
                UUID.randomUUID(),
                targetKey,
                targetName,
                contact,
                messageVariable,
                state,
                resultMessage,
                attemptNo,
                title,
                content,
                resourceJson,
                targetUpload,
                createdAt,
                lastUploadedAt,
                Map.of()
                );
    }



}
