package com.ums.schedule.email.domain.target;

import com.ums.schedule.common.code.email.EmailResultCode;
import com.ums.schedule.email.application.target.reader.listener.model.SendTargetReadRow;
import com.ums.schedule.common.util.JsonUtil;
import com.ums.schedule.email.adaptor.out.persistence.jpa.entity.SendTargetRequestEntity;
import com.ums.schedule.email.domain.common.SendResult;
import com.ums.schedule.email.domain.target.group.DomainGroup;
import com.ums.schedule.email.domain.target.receiver.Receiver;
import com.ums.schedule.email.domain.target.state.SendTargetCreateState;
import com.ums.schedule.email.domain.target.state.SendTargetErrorState;
import com.ums.schedule.email.domain.target.state.SendTargetRequestState;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SendTargetRequest {
    private Long id;
    private Long sendRequestId;
    private DomainGroup group;
    private Receiver receiver;
    private SendTargetRequestState state;
    private SendResult result;
    private Map<String, Object> messageVariable = new HashMap<>();


    public static SendTargetRequest of(Long sendRequestId, DomainGroup group, SendTargetReadRow row) {
        SendTargetRequest targetRequest = new SendTargetRequest(sendRequestId);
        targetRequest.assignDomainGroup(group);
        targetRequest.assignReceiver(row.getMessageVariable());
        targetRequest.assignTargetRequestState(new SendTargetCreateState());
        targetRequest.assignMessageVariable(row.rowNo(), row.getMessageVariable());
        return targetRequest;
    }

    private SendTargetRequest(Long sendRequestId) {
        this.sendRequestId = sendRequestId;
    }


    private void assignReceiver(Map<String, Object> targetData) {
        this.receiver = Receiver.of(targetData);
    }

    private void assignDomainGroup(DomainGroup group) {
        this.group = group;
    }

    public static SendTargetRequest failureOf(Long sendRequestId, int rowNo, Map<String, Object> targetData) {
        SendTargetRequest targetRequest = new SendTargetRequest();
        targetRequest.assignDomainGroup(DomainGroup.of("null"));
        targetRequest.assignRequestId(sendRequestId);
        targetRequest.assignReceiver(targetData);
        targetRequest.assignMessageVariable(rowNo, targetData);
        return targetRequest;
    }

    private void assignRequestId(Long sendRequestId) {
        this.sendRequestId = sendRequestId;
    }


    private void assignTargetRequestState(SendTargetRequestState state) {
        this.state = state;
    }

    private void assignMessageVariable(int rowNo, Map<String, Object> targetData) {
        this.messageVariable = generateMessageVariables(rowNo, targetData);
    }

    private Map<String, Object> generateMessageVariables(int rowNo, Map<String, Object> targetData) {
        Map<String, Object> messageVariable = new HashMap<>();
        messageVariable.put("row_no", rowNo);
        messageVariable.putAll(targetData);
        return messageVariable;
    }

    public String toMessageVariable() {
        String json = JsonUtil.toJson(this.messageVariable);
        if (!StringUtils.hasText(json)) {
            return this.messageVariable.toString();
        }
        return json;
    }

    public Integer getRow() {
        return Optional.ofNullable(this.messageVariable.get("row_no"))
                .map(String::valueOf)
                .map(Integer::parseInt)
                .orElse(-1);
    }

    public SendTargetRequestEntity toEntity() {
        return SendTargetRequestEntity.fromDomain(this);
    }

    public static SendTargetRequest fromEntity(SendTargetRequestEntity entity) {
        SendTargetRequest request = new SendTargetRequest(entity);
        return request;
    }

    private SendTargetRequest(SendTargetRequestEntity entity) {
        this.id = entity.getId();
        this.sendRequestId = entity.getSendRequest().getId();
        this.group = new DomainGroup(entity.getGroupId(), entity.getGroupKey());
        this.receiver = new Receiver(entity.getTargetKey(), entity.getTargetName(), entity.getEmail());
        this.messageVariable = JsonUtil.toMap(entity.getMessageVariable(), Object.class);
        this.result = entity.getSendResult();
    }

    public void setStateAndSendResult(SendTargetRequestState state, SendResult result) {
        this.state = state;
        this.result = result;
    }
}
