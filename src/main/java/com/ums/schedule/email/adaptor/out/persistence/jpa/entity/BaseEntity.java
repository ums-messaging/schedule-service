package com.ums.schedule.email.adaptor.out.persistence.jpa.entity;

import com.github.f4b6a3.uuid.UuidCreator;
import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@MappedSuperclass
public abstract class BaseEntity {
    @Column(name = "created_at", nullable = false)
    protected LocalDateTime createdAt;
    @Column(name = "last_updated_at")
    protected LocalDateTime lastUpdatedAt;

    @Transient
    protected boolean isNew;
    protected void assignId(UUID toId, String fromId) {
        if(StringUtils.hasText(fromId)) {
            toId = UuidCreator.fromString(fromId);
            return;
        }
        toId = UuidCreator.getTimeOrdered();
    }

    @PrePersist
    public void prePersist() {
        if(createdAt == null) {
            this.createdAt = LocalDateTime.now();
            this.isNew = true;
        }
    }
    @PostLoad
    public void postLoad() {
        this.lastUpdatedAt = LocalDateTime.now();
        this.isNew = false;
    }
}
