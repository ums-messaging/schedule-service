package com.ums.schedule.domain.target;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ums.schedule.code.send.ContentTypeEnum;
import com.ums.schedule.code.send.SendTargetStatusEnum;
import com.ums.schedule.code.send.TargetColumnEnum;
import com.ums.schedule.domain.channel.email.attachment.Attachment;
import com.ums.schedule.application.target.dto.SendTargetDto;
import com.ums.schedule.domain.request.SendRequest;
import com.ums.schedule.domain.target.status.SendTargetCreatedStatus;
import com.ums.schedule.domain.target.status.SendTargetReadyStatus;
import com.ums.schedule.domain.target.status.SendTargetStatus;
import com.ums.schedule.domain.channel.email.message.EmailTemplate;
import com.ums.schedule.domain.target.upload.TargetUpload;
import com.ums.schedule.domain.channel.email.exception.TemplateContentRequiredException;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import jakarta.persistence.Transient;
import lombok.Getter;

import java.io.IOException;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Getter
//@Entity
public class SendTarget {
    private String id;
    private String targetKey;
    private String targetName;
    private String contact;
    private String messageVariable;
    private TargetUpload targetUpload;

    private SendTargetStatusEnum status; // ready, retrying, success, fail, sending,

    private Integer attemptNo;
    private ContentTypeEnum contentType;

    private String title;
    private String content;

    private SendRequest sendRequest;
    private List<Attachment> attachment = new ArrayList<>();

    private LocalDateTime createdAt;
    private LocalDateTime lastUploadedAt;

    @Transient
    private SendTargetStatus state;

    @Transient
    private Map<String, Object> dataParam = new HashMap<>();

    public static SendTarget of(SendTargetDto dto, EmailTemplate template) {
        SendTarget sendTarget = new SendTarget(dto.targetData());
        sendTarget.changeTargetStatus(new SendTargetCreatedStatus());
        sendTarget.dataParamToJson(dto.dataParam());
        sendTarget.makeMessage(template);
        return sendTarget;
    }

    public void resolveContact(SendRequest request, Map<TargetColumnEnum, String> targetData) {
        this.contact = request.getContact(targetData);
    }

    private void dataParamToJson(Map<String, Object> dataParam) {
        ObjectMapper mapper = new ObjectMapper();
        String json = null;
        try {
            json = mapper.writeValueAsString(dataParam);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        this.dataParam = dataParam;
        this.messageVariable = json;
    }

    private void changeTargetStatus(SendTargetStatus state) {
        this.state = state;
        this.status = state.currentStatusCode();
        this.lastUploadedAt = LocalDateTime.now();
    }

    private SendTarget(Map<TargetColumnEnum, String> targetMap) {
        this.targetKey = targetMap.get(TargetColumnEnum.TARGET_KEY);
        this.targetName = targetMap.get(TargetColumnEnum.TARGET_NAME);
        this.createdAt = LocalDateTime.now();
        this.attemptNo = 1;
    }

    public SendTarget applySendRequest(SendRequest sendRequest) {
        this.sendRequest = sendRequest;
        this.contentType = sendRequest.getContentType();
        return this;
    }

    public String parse(String content) {
        Set<String> keySet = getKeySet(content);
        for(String key : keySet) {
            String value = (String) this.dataParam.getOrDefault(key, null);
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
        Set<String> keySet = getKeySet(content);
        while(matcher.find()) {
            keySet.add(matcher.group(1));
        }
        return keySet;
    }

    private void makeMessage(EmailTemplate template) {
        makeTitle(template);
        makeBody(template);
    }

    private void makeTitle(EmailTemplate template) {
        this.title = parse(template.getTitle());
    }

    private void makeBody(EmailTemplate template) {
        String header = compile(template.getHeader());
        String body = compile(template.getBody());
        String footer = compile(template.getFooter());
        this.content = header + body + footer;
    }

    private String compile(Template template) {
        return Optional.ofNullable(template)
                .map(t -> {
                    StringWriter writer = new StringWriter();
                    try {
                        t.process(dataParam, writer);
                    } catch (IOException | TemplateException e) {
                        throw TemplateContentRequiredException.ofTemplateKey();
                    }
                    return writer.toString();
                })
                .orElseThrow();
    }

    public SendTarget toReady() {
        changeTargetStatus(new SendTargetReadyStatus());
        return this;
    }
}
