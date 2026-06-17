package com.ums.schedule.domain.sendrequest.target;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.f4b6a3.uuid.UuidCreator;
import com.ums.schedule.application.sendrequest.message.email.result.EmailTargetMessageResult;
import com.ums.schedule.common.util.JsonUtil;
import com.ums.schedule.domain.sendrequest.converter.UuidBinaryConverter;
import com.ums.schedule.domain.sendrequest.template.email.EmailTemplate;
import com.ums.schedule.domain.sendrequest.target.converter.SendTargetStatusConverter;
import com.ums.schedule.domain.sendrequest.target.code.SendTargetStatusEnum;
import com.ums.schedule.domain.sendrequest.target.code.TargetColumnEnum;
import com.ums.schedule.domain.sendrequest.template.ChannelTemplate;
import com.ums.schedule.application.sendrequest.target.data.TargetMessageData;
import com.ums.schedule.domain.sendrequest.target.state.SendTargetReadyState;
import com.ums.schedule.domain.sendrequest.target.state.SendTargetState;
import com.ums.schedule.domain.sendrequest.template.exception.TemplateContentRequiredException;
import com.ums.schedule.domain.sendrequest.target.upload.TargetUploadReport;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
    private SendTargetStatusEnum status;

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
    private SendTargetState state;


    public static SendTarget of(TargetUploadReport targetUpload, TargetMessageData dto, EmailTemplate template) {
        SendTarget sendTarget = new SendTarget(dto.targetData());
        sendTarget.changeTargetStatus(new SendTargetReadyState());
        sendTarget.dataParamToJson(dto.dataParam());
        sendTarget.applyTargetUpload(targetUpload);
        sendTarget.applyContact(targetUpload, dto.targetData());
        return sendTarget;
    }

    public static SendTarget failureOf(TargetMessageData dto, String reason) {
        SendTarget target = new SendTarget(dto.targetData());
        return target;
    }

    private void applyContact(TargetUploadReport targetUpload, Map<TargetColumnEnum, String> targetData) {
//        this.contact = targetUpload.resolveTargetContact(targetData);
    }

    private void applyTargetUpload(TargetUploadReport targetUpload) {
        this.targetUpload = targetUpload;
    }

    private void dataParamToJson(Map<String, Object> dataParam) {
        ObjectMapper mapper = new ObjectMapper();
        String json = null;
        try {
            json = mapper.writeValueAsString(dataParam);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        this.messageVariable = json;
    }

    public void changeTargetStatus(SendTargetState state) {
        this.state = state;
        this.status = state.currentStatusCode();
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
            String value = (String) dataParam.getOrDefault(key, null);
            if(value == null) {
                throw new RuntimeException();
            }
            content = content.replace("#{".concat(key).concat("}"), value);
        }
        return content;
    }

    private Set<String> getKeySet(String content) {
        Pattern pattern = Pattern.compile("#\\{([^}]+)\\}");
        Matcher matcher = pattern.matcher(content);
        Set<String> keySet = new HashSet<>();
        while(matcher.find()) {
            keySet.add(matcher.group(1));
        }
        return keySet;
    }

    private void makeMessage(ChannelTemplate template) {
        makeTitle(template);
        makeBody(template);
    }

    private void makeTitle(ChannelTemplate template) {
        this.title = template.getTitle(this);
    }

    private void makeBody(ChannelTemplate template) {
        this.content = template.compile(this);
    }

    public String compile(Template template) {
        return Optional.ofNullable(template)
                .map(t -> {
                    StringWriter writer = new StringWriter();
                    try {
                        Map<String, Object> dataParam = getDataParam();
                        t.process(dataParam, writer);
                    } catch (IOException | TemplateException e) {
                        throw TemplateContentRequiredException.ofTemplateKey();
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

    public void assignResources(List<EmailTargetMessageResult> resources) {
        if (resources.size() > 00) {
            String json = JsonUtil.toJson(resources);
            this.resourceJson = json;
        }
    }

    public Map<String, Object> getDataParam() {
        return JsonUtil.toMap(this.messageVariable, Object.class);
    }
}
