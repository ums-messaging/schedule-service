package com.ums.schedule.domain.target;

import com.github.f4b6a3.uuid.UuidCreator;
import com.ums.schedule.common.exception.BusinessException;
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

    @Column(name = "result_message")
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

    public static SendTarget of(TargetUploadReport targetUpload, TargetMessageData targetMessageData, TargetMessage targetMessage) {
        SendTarget target = new SendTarget();
        target.initializeTargetData(targetMessageData);
        target.changeTargetStatus(new SendTargetCreateState());
        target.dataParamToJson(targetMessageData);
        target.applyTargetUpload(targetUpload);
        target.assignTargetMessage(targetMessage);
        return target;
    }

    public static SendTarget failureOf(TargetUploadReport report, TargetMessageData row, String errorMessage) {
        SendTarget target = new SendTarget();
        try {
            target.initializeTargetData(row);
            target.dataParamToJson(row);
            target.onError(errorMessage);
            return target;
        } catch (BusinessException e) {
            target.onError(errorMessage);
        }
        target.applyTargetUpload(report);
        return target;
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


    protected void onFailure(String errorMessage) {
        changeTargetStatus(new SendTargetFailState());
        assignResultMessage(errorMessage);
    }

    protected void assignResultMessage(String message) {
        this.resultMessage = message;
    }

    public void assignContact(String contact) {
        this.contact = contact;
    }

    private void applyTargetUpload(TargetUploadReport targetUpload) {
        this.targetUpload = targetUpload;
    }

    private void dataParamToJson(TargetMessageData dataParam) {
        String messageVariable = JsonUtil.toJson(dataParam.getTargetParam());
        if(!StringUtils.hasText(messageVariable)) {
            messageVariable = dataParam.toString();
        }
        this.messageVariable = messageVariable;
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
