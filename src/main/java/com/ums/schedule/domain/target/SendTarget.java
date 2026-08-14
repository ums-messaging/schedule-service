package com.ums.schedule.domain.target;

import com.ums.schedule.application.ums.common.target.context.SendTargetCreateContext;
import com.ums.schedule.common.code.target.SendTargetRowStatus;
import com.ums.schedule.common.util.JsonUtil;
import com.ums.schedule.domain.request.converter.UuidBinaryConverter;
import com.ums.schedule.domain.target.state.SendTargetCreateState;
import com.ums.schedule.domain.target.state.SendTargetFailState;
import com.ums.schedule.domain.target.converter.SendTargetStatusConverter;
import com.ums.schedule.common.code.target.SendTargetColumn;
import com.ums.schedule.application.sendrequest.target.data.TargetMessageData;
import com.ums.schedule.domain.target.state.SendTargetState;
import com.ums.schedule.domain.target.upload.TargetUploadReport;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.*;

@Getter
@Entity
@Table(name = "send_target",
        uniqueConstraints = {
                @UniqueConstraint(
                        name="uq_target_key",
                        columnNames = {"upload_id", "target_key"}
                ),
                @UniqueConstraint(
                        name = "uq_target_contact",
                        columnNames = {"upload_id", "contact"}
                )
        }
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class SendTarget {
    @Id
    @Column
    @UuidGenerator
    @GeneratedValue
    @Convert(converter = UuidBinaryConverter.class)
    private UUID id;

    @Column(name = "target_key")
    private String targetKey;

    @Column(name = "target_name")
    private String targetName;

    @Column(name = "contact")
    private String contact;

    @Column(name = "message_variable")
    private String messageVariable;

    @Column(name = "status", nullable = false)
    @Convert(converter = SendTargetStatusConverter.class)
    private SendTargetState state;

    @Lob
    @Column(name = "result_message", columnDefinition = "LONGTEXT")
    private String resultMessage;

    @Column(name = "attempt_no", nullable = false)
    private Integer attemptNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "upload_id", nullable = false)
    private TargetUploadReport targetUpload;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "last_updated_at")
    private LocalDateTime lastUploadedAt;

    @Transient
    private Map<String, Object> dataParamMap = new HashMap<>();

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "target_message_id")
    private TargetMessage targetMessage;

    public static SendTarget of(TargetUploadReport targetUploadReport, SendTargetCreateContext context) {
        SendTarget target = new SendTarget();
        target.initializeTargetData(context.targetMessageData());
        target.initializeState(context.state());
        target.dataParamToJson(context.partitionNo(), context.targetMessageData());
        target.applyTargetUpload(targetUploadReport);
        target.assignTargetMessage(context.targetMessage());
        return target;
    }

    private void initializeState(SendTargetRowStatus state) {
        if(state == SendTargetRowStatus.FAIL) {
            changeTargetStatus(new SendTargetFailState());
            return;
        }
        changeTargetStatus(new SendTargetCreateState());
    }

    public void assignTargetMessage(TargetMessage targetMessage) {
        Objects.requireNonNull(targetMessage, "target_message");
        targetMessage.assignSendTarget(this);
        this.targetMessage = targetMessage;
    }


    private void initializeTargetData(TargetMessageData targetMessageData) {
        Map<SendTargetColumn, String> targetMap = targetMessageData.targetData();
        this.targetKey = targetMap.get(SendTargetColumn.TARGET_KEY);
        this.targetName = targetMap.get(SendTargetColumn.TARGET_NAME);
    }

    protected void assignResultMessage(String message) {
        this.resultMessage = message;
    }

    public void assignContact(String contact) {
        this.contact = contact;
    }

    private void applyTargetUpload(TargetUploadReport targetUpload) {
        this.targetUpload = Objects.requireNonNull(targetUpload, "target upload is required.");
    }

    private void dataParamToJson(Integer partitionNo, TargetMessageData dataParam) {
        Map<String, Object> newDataParam = putDataParam(partitionNo, dataParam);
        String messageVariable = JsonUtil.toJson(newDataParam);

        if(!StringUtils.hasText(messageVariable)) {
            this.messageVariable = dataParam.toString();
            return;
        }
        this.messageVariable = messageVariable;
    }

    private Map<String, Object> putDataParam(Integer partitionNo, TargetMessageData dataParam) {
        Map<String, Object> targetData = new HashMap<>();
        targetData.put("partitionNo", partitionNo);
        dataParam.getTargetParam().entrySet()
                .stream()
                .forEach(v -> {
                    targetData.put(v.getKey(), v.getValue());
                });
        return targetData;
    }

    public void changeTargetStatus(SendTargetState state) {
        this.state = state;
        this.lastUploadedAt = LocalDateTime.now();
    }

    public SendTarget onError(String reason) {
        changeTargetStatus(new SendTargetFailState());
        assignResultMessage(reason);
        return this;
    }

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.attemptNo = Optional.ofNullable(this.attemptNo).orElse(1);
    }
}
