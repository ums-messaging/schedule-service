package com.ums.schedule.domain.target;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ums.schedule.code.send.ContentTypeEnum;
import com.ums.schedule.code.send.SendTargetStatusEnum;
import com.ums.schedule.code.send.TargetColumnEnum;
import com.ums.schedule.domain.channel.ChannelTemplate;
import com.ums.schedule.application.target.dto.SendTargetDto;
import com.ums.schedule.domain.target.state.SendTargetReadyState;
import com.ums.schedule.domain.target.state.SendTargetState;
import com.ums.schedule.domain.channel.email.exception.TemplateContentRequiredException;
import com.ums.schedule.domain.target.upload.TargetUpload;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Transient;
import lombok.Getter;

import java.io.IOException;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Getter
@Entity
public class SendTarget {
    @Id
    private String id;
    private String targetKey;
    private String targetName;
    private String contact;
    private String messageVariable;



    private SendTargetStatusEnum status; // ready, retrying, success, fail, sending,

    private Integer attemptNo;
    private ContentTypeEnum contentType;

    private String title;
    private String content;

    @ManyToOne
    private TargetUpload targetUpload;

    private LocalDateTime createdAt;
    private LocalDateTime lastUploadedAt;

    @Transient
    private SendTargetState state;

    @Transient
    private Map<String, Object> dataParam = new HashMap<>();

    public static SendTarget of(SendTargetDto dto, ChannelTemplate template) {
        SendTarget sendTarget = new SendTarget(dto.targetData());
        sendTarget.changeTargetStatus(new SendTargetReadyState());
        sendTarget.dataParamToJson(dto.dataParam());
        sendTarget.makeMessage(template);
        return sendTarget;
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

    private void changeTargetStatus(SendTargetState state) {
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

    private void makeMessage(ChannelTemplate template) {
        makeTitle(template);
        makeBody(template);
    }

    private void makeTitle(ChannelTemplate template) {
        this.title = template.getTitle(this);
    }

    private void makeBody(ChannelTemplate template) {
        this.contact = template.compile(this);
    }

    public String compile(Template template) {
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
        changeTargetStatus(new SendTargetReadyState());
        return this;
    }
}
