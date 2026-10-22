package com.ums.schedule.email.adaptor.out.persistence.jpa.entity;

import com.github.f4b6a3.tsid.TsidCreator;
import com.ums.schedule.common.code.email.EmailResultCode;
import com.ums.schedule.email.domain.common.SendResult;
import com.ums.schedule.email.domain.target.SendTargetRequest;
import com.ums.schedule.email.domain.target.group.DomainGroup;
import com.ums.schedule.email.domain.target.receiver.Receiver;
import com.ums.schedule.email.domain.target.state.SendTargetRequestState;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

import java.util.Map;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table( name = "send_target_request",
        uniqueConstraints = {
        @UniqueConstraint(
                name="uq_target_key",
                columnNames = {"send_request_id", "target_key"}
        ),
        @UniqueConstraint(
                name = "uq_target_email",
                columnNames = {"send_request_id", "email"}
        )
    }
)
public class SendTargetRequestEntity extends BaseEntity {
    @Id
    private Long id;

    private String targetKey;
    private String targetName;
    private String email;

    private Long groupId;
    private String groupKey;

    private String status;
    private String resultCode;
    @Column(name = "result_message", length = 2000)
    private String resultMessage;

    @Column(name = "message_variable", length = 4000)
    private String messageVariable;

    @ManyToOne
    @JoinColumn(name = "send_request_id")
    private EmailSendRequestEntity sendRequest;

    public static SendTargetRequestEntity fromDomain(SendTargetRequest domain) {
        SendTargetRequestEntity entity = new SendTargetRequestEntity();
        entity.generateId(domain.getId());
        entity.assignDomainGroup(domain.getGroup());
        entity.assignMessageVariable(domain.toMessageVariable());
        entity.assignReceiver(domain.getReceiver());
        entity.assignStatus(domain.getState());
        entity.assignSendResult(domain.getResult());
        return entity;
    }

    public void assignSendRequest(EmailSendRequestEntity sendRequest) {
        this.sendRequest = sendRequest;
    }

    private void assignSendResult(SendResult result) {
        if(result != null) {
            this.resultCode = result.resultCode().code();
            this.resultMessage = result.resultMessage();
        }
    }

    public SendTargetRequest toDomain() {
        return SendTargetRequest.fromEntity(this);
    }

    private void assignStatus(SendTargetRequestState state) {
        this.status = state.currentStateCode().name();
    }

    private void assignReceiver(Receiver receiver) {
        this.targetKey = receiver.getTargetKey();
        this.targetName = receiver.getName();
        this.email = receiver.getEmail();
    }

    private void assignMessageVariable(String messageVariable) {
        this.messageVariable = messageVariable;
    }


    private void assignDomainGroup(DomainGroup group) {
        this.groupId = group.groupId();
        this.groupKey = group.groupKey();
    }

    private void generateId(Long id) {
        if(id != null) {
            this.id = id;
            this.isNew = false;
            return;
        }
        this.id = TsidCreator.getTsid().toLong();
    }

    public SendResult getSendResult() {
        if(StringUtils.hasText(resultCode)) {
            return new SendResult(EmailResultCode.valueOf(resultCode), resultMessage);
        }
        return null;
    }
}
