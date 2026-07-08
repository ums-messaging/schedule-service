package com.ums.schedule.domain.sendrequest.target;

import com.github.f4b6a3.uuid.UuidCreator;
import com.ums.schedule.application.message.email.EmailResourceCommand;
import com.ums.schedule.common.util.JsonUtil;
import com.ums.schedule.domain.sendrequest.converter.UuidBinaryConverter;
import com.ums.schedule.domain.sendrequest.target.exeption.SendTargetContentParsingException;
import com.ums.schedule.domain.sendrequest.target.exeption.SendTargetTitleParsingException;
import com.ums.schedule.domain.sendrequest.target.state.SendTargetCreateState;
import com.ums.schedule.domain.sendrequest.target.state.SendTargetFailState;
import com.ums.schedule.domain.sendrequest.template.email.EmailTemplate;
import com.ums.schedule.domain.sendrequest.target.converter.SendTargetStatusConverter;
import com.ums.schedule.domain.sendrequest.target.code.TargetColumnEnum;
import com.ums.schedule.application.sendrequest.target.data.TargetMessageData;
import com.ums.schedule.domain.sendrequest.target.state.SendTargetState;
import com.ums.schedule.domain.sendrequest.target.upload.TargetUploadReport;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class SendTarget {
    @Id
    @Column(columnDefinition = "BINARY(16)")
    @Convert(converter = UuidBinaryConverter.class)
    private UUID id;

    @Column(name = "target_key", nullable = false)
    private String targetKey;
    @Column(name = "target_name", nullable = false)
    private String targetName;

    @Column(name = "contact", nullable = false)
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

    @Column(name = "title")
    private String title;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "resource_json")
    private String resourceJson;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "upload_id", nullable = false)
    private TargetUploadReport targetUpload;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "last_updated_at")
    private LocalDateTime lastUploadedAt;

    @Transient
    private Map<String, Object> dataParamMap = new HashMap<>();


    public static SendTarget of(TargetUploadReport targetUpload, TargetMessageData dto, EmailTemplate template) {
        SendTarget sendTarget = new SendTarget(dto.targetData());
        sendTarget.changeTargetStatus(new SendTargetCreateState());
        sendTarget.dataParamToJson(dto);
        sendTarget.applyTargetUpload(targetUpload);
        sendTarget.generateMessage(template);
        return sendTarget;
    }

    private void generateMessage(EmailTemplate template) {
        this.title = parse(template.getTitle());
        this.content = compile(template.getBody());
    }

    public static SendTarget failureOf(TargetMessageData dto, String reason) {
        SendTarget target = new SendTarget(dto.targetData());
        target.changeTargetStatus(new SendTargetFailState());
        target.assignResultMessage(reason);
        return target;
    }

    private void assignResultMessage(String message) {
        this.resultMessage = message;
    }


    public void assignContact(String contact) {
        this.contact = contact;
    }

    private void applyTargetUpload(TargetUploadReport targetUpload) {
        this.targetUpload = targetUpload;
    }

    private void dataParamToJson(TargetMessageData dataParam) {
        this.messageVariable = JsonUtil.toJson(dataParam.getTargetParam());
    }

    public void changeTargetStatus(SendTargetState state) {
        this.state = state;
        this.lastUploadedAt = LocalDateTime.now();
    }

    private SendTarget(Map<TargetColumnEnum, String> targetMap) {
        this.targetKey = targetMap.get(TargetColumnEnum.TARGET_KEY);
        this.targetName = targetMap.get(TargetColumnEnum.TARGET_NAME);
    }

    public SendTarget onError(String reason) {
        return this;
    }


    public String parse(String content) {
        Set<String> keySet = getKeySet(content);
        for(String key : keySet) {
            Map<String, Object> dataParam = getDataParam();
            Object value = dataParam.get(key);
            if(value == null) {
                throw SendTargetTitleParsingException.of(targetKey, key);
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

    public String compile(Template template) {
        return Optional.ofNullable(template)
                .map(t -> {
                    StringWriter writer = new StringWriter();
                    try {
                        Map<String, Object> dataParam = getDataParam();
                        t.process(dataParam, writer);
                    } catch (IOException | TemplateException e) {
                        throw SendTargetContentParsingException.of(e);
                    }
                    return writer.toString();
                })
                .orElseThrow();
    }


    @PrePersist
    public void prePersist() {
        if(this.id == null) {
            this.id = UuidCreator.getTimeOrdered();
        }

        this.createdAt = LocalDateTime.now();
        this.attemptNo = Optional.ofNullable(this.attemptNo).orElse(1);
    }

    public List<EmailResourceCommand> generateAttachments(List<EmailResourceCommand> resources) {
        if(!resources.isEmpty()) {
            this.resourceJson = JsonUtil.toJson(resources);
        }
        return resources;
    }

    public Map<String, Object> getDataParam() {
        if(!StringUtils.hasText(this.messageVariable) && dataParamMap.isEmpty()) {
           return Collections.emptyMap();
        }
        return JsonUtil.toMap(this.messageVariable, Object.class);
    }
}
