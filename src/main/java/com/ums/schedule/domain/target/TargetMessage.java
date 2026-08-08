package com.ums.schedule.domain.target;
import com.ums.schedule.domain.request.converter.UuidBinaryConverter;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.util.Objects;
import java.util.UUID;

@Entity
@Getter
@Inheritance(strategy = InheritanceType.JOINED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public abstract class TargetMessage {
    @Id
    @UuidGenerator
    @GeneratedValue
    @Convert(converter = UuidBinaryConverter.class)
    private UUID id;

    @OneToOne(mappedBy = "targetMessage")
    private SendTarget sendTarget;

    public void assignSendTarget(SendTarget sendTarget) {
        Objects.requireNonNull(sendTarget, "send_target is not null.");
        this.sendTarget = sendTarget;
    }

    public UUID getMessageId() {
        return messageId();
    }
    protected abstract UUID messageId();
}
