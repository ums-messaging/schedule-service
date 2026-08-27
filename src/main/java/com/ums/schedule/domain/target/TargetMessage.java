package com.ums.schedule.domain.target;
import com.github.f4b6a3.tsid.TsidCreator;
import com.ums.schedule.application.sendrequest.target.data.TargetMessageData;
import com.ums.schedule.application.ums.common.target.result.SendTargetResult;
import com.ums.schedule.application.ums.email.generator.policy.model.EmailConvertPolicy;
import com.ums.schedule.common.code.target.SendTargetColumn;
import com.ums.schedule.common.code.target.SendTargetResultCode;
import com.ums.schedule.common.util.JsonUtil;
import com.ums.schedule.domain.target.converter.SendTargetStatusConverter;
import com.ums.schedule.domain.target.state.SendTargetCreateState;
import com.ums.schedule.domain.target.state.SendTargetFailState;
import com.ums.schedule.domain.target.state.SendTargetState;
import com.ums.schedule.domain.target.upload.TargetUploadReport;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Persistable;
import org.springframework.util.StringUtils;

import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Entity
@Getter
@DiscriminatorColumn(name = "channel_type")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class TargetMessage implements Persistable<Long> {
    @Id
    protected Long id;

    @Column(name = "group_id", nullable = false)
    protected Long groupId;

    @Column(name = "target_key")
    protected String targetKey;

    @Column(name = "target_name")
    protected String targetName;

    @Column(name = "contact")
    protected String contact;

    @Column(name = "message_variable")
    protected String messageVariable;

    @Column(name = "status", nullable = false)
    @Convert(converter = SendTargetStatusConverter.class)
    protected SendTargetState state;

    @Lob
    @Column(name = "result_message", columnDefinition = "LONGTEXT")
    protected String resultMessage;

    @Column(name = "attempt_no", nullable = false)
    protected Integer attemptNo;

    @Column(name = "created_at", nullable = false)
    protected LocalDateTime createdAt;

    @Column(name = "last_updated_at")
    protected LocalDateTime lastUploadedAt;

    @Transient
    protected Map<String, Object> dataParamMap = new HashMap<>();

    @Transient
    protected TargetMessageData targetMessageData;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "upload_id", nullable = false)
    protected TargetUploadReport targetUploadReport;

    @Transient
    protected boolean isNew;

    protected TargetMessage(TargetUploadReport targetUploadReport, SendTargetResult target) {
        generateId();
        initializeState(target.resultCode(), target.resultMessage());
        assignTargetUploadReport(targetUploadReport);
        initializeTargetData(target.targetData());
        initializeGroupId(target.groupId());
        dataParamToJson(target.partitionNo(), target.rowNo(), target.targetData());
        initializeCreatedAt();
        initializeAttemptNo();
    }

    private void initializeGroupId(Long groupId) {
        this.groupId = groupId;
    }

    private void generateId() {
        this.id = TsidCreator.getTsid().toLong();
        this.isNew = true;
    }

    private void assignTargetUploadReport(TargetUploadReport targetUploadReport) {
        this.targetUploadReport =
                Objects.requireNonNull(targetUploadReport, "target_upload_report is not null.");
    }
    private void initializeAttemptNo() {
        this.attemptNo = 0;
    }
    private void initializeCreatedAt() {
        this.createdAt = LocalDateTime.now();
    }
    private void initializeState(SendTargetResultCode resultCode, String resultMessage) {
        if(resultCode != SendTargetResultCode.SUCCESS) {
            this.state = new SendTargetFailState();
            this.resultMessage = resultMessage;
            return;
        }
        this.state = new SendTargetCreateState();
    }
    private void initializeTargetData(TargetMessageData targetMessageData) {
        Map<SendTargetColumn, String> targetMap = targetMessageData.targetData();
        this.targetKey = assignTargetData(SendTargetColumn.TARGET_KEY, targetMap.get(SendTargetColumn.TARGET_KEY));
        this.targetName = assignTargetData(SendTargetColumn.TARGET_NAME, targetMap.get(SendTargetColumn.TARGET_NAME));
        this.contact = assignContact(targetMap);
    }

    protected String assignTargetData(SendTargetColumn column, String value) {
        if(!StringUtils.hasText(value)) {
            onError(SendTargetResultCode.TARGET_DATA_REQUIRED, column.description());
        }
        return value;
    }

    private void dataParamToJson(Integer partitionNo, Integer rowNo, TargetMessageData dataParam) {
        Map<String, Object> newDataParam = putDataParam(partitionNo, rowNo, dataParam);
        this.dataParamMap = newDataParam;

        String messageVariable = JsonUtil.toJson(newDataParam);

        if(!StringUtils.hasText(messageVariable)) {
            this.messageVariable = dataParam.toString();
            return;
        }
        this.messageVariable = messageVariable;
    }

    private Map<String, Object> putDataParam(Integer partitionNo, Integer rowNo, TargetMessageData dataParam) {
        Map<String, Object> targetData = new HashMap<>();
        targetData.put("rowNo", rowNo);
        targetData.put("partitionNo", partitionNo);
        dataParam.getTargetParam().entrySet()
                .stream()
                .forEach(v -> {
                    targetData.put(v.getKey(), v.getValue());
                });
        return targetData;
    }

    protected void changeTargetStatus(SendTargetState state) {
        this.state = state;
        this.lastUploadedAt = LocalDateTime.now();
    }
    @PostPersist
    public void postPersist() {
        this.isNew = false;
    }

    public UUID getMessageId() {
        return messageId();
    }
    protected abstract UUID messageId();

    public void onError(SendTargetResultCode errorCode, String... args) {
        changeTargetStatus(new SendTargetFailState());
        assignResultMessage(String.format(errorCode.description(), args));
    }
    public String parse(String field, String content) {
        if(!StringUtils.hasText(content)) {
            onError(SendTargetResultCode.TEMPLATE_EMPTY, field);
            return null;
        }
        Set<String> keySet = getKeySet(content);
        for(String key : keySet) {
            Object value = dataParamMap.get(key);
            if(value == null) {
                onError(SendTargetResultCode.TARGET_VARIABLE_REQUIRED, field, key);
                return null;
            }
            String valueTo = String.valueOf(value);
            content = content.replace("${".concat(key).concat("}"), valueTo);
        }
        return content;
    }

    private Set<String> getKeySet(String content) {
        Pattern pattern = Pattern.compile("\\$\\{([^}]+)\\}");
        Matcher matcher = pattern.matcher(content);
        Set<String> keySet = new HashSet<>();
        while(matcher.find()) {
            keySet.add(matcher.group(1));
        }
        return keySet;
    }
    private void assignResultMessage(String resultMessage) {
        if(resultMessage.length() > 128) {
            this.resultMessage = resultMessage.substring(0, 127);
            return;
        }
        this.resultMessage = resultMessage;
    }

    @PrePersist
    public void prePersist() {
        this.attemptNo = Optional.ofNullable(attemptNo).orElse(3);
        this.createdAt = LocalDateTime.now();
    }

    protected abstract String assignContact(Map<SendTargetColumn, String> targetMap);
}
