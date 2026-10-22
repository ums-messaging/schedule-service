package com.ums.schedule.email.domain.event;

import com.ums.schedule.common.code.email.EmailResultCode;
import com.ums.schedule.common.code.request.SendGroupEventType;
import com.ums.schedule.email.adaptor.out.persistence.jpa.entity.SendTargetGroupEventEntity;
import com.ums.schedule.email.domain.target.SendTargetError;
import com.ums.schedule.email.domain.target.SendTargetRequest;
import com.ums.schedule.email.domain.target.group.DomainGroup;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SendTargetGroupEvent {
    private Long id;
    private DomainGroup domainGroup;
    private Long sendRequestId;
    private SendGroupEventType eventType;
    private EmailResultCode resultCode;
    private String resultMessage;
    private Integer totalCount;
    private List<SendTargetRequest> successList = new ArrayList<>();
    private List<SendTargetError> errorList = new ArrayList<>();
    private String issuedAt;

    public static SendTargetGroupEvent of(DomainGroupEvent event, SendGroupEventResult result, SendTargetResultList resultList) {
        SendTargetGroupEvent groupEvent = new SendTargetGroupEvent(event);
        groupEvent.assignTargetReport(resultList);
        groupEvent.assignResult(result);
        return groupEvent;
    }

    private void assignTargetReport(SendTargetResultList resultList) {
        int successCount = resultList.successList().size();
        int failCount = resultList.errorList().size();
        this.totalCount = successCount + failCount;
        this.successList = resultList.successList();
        this.errorList = resultList.errorList();
    }

    private void assignResult(SendGroupEventResult result) {
        this.resultCode = result.resultCode();
        this.resultMessage = result.resultMessage();
    }

    private SendTargetGroupEvent(DomainGroupEvent event) {
        this.domainGroup = event.domainGroup();
        this.sendRequestId = event.sendRequestId();
        this.eventType = event.eventType();
        this.issuedAt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now());
    }

    public SendTargetGroupEventEntity toEntity() {
        return SendTargetGroupEventEntity.fromDomain(this);
    }
}
