package com.ums.schedule.orchestrator.adaptor.out.persistance.jpa.entity;


import com.ums.schedule.orchestrator.domain.schedule.Schedule;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "schedule")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ScheduleEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(name = "schedule_name", nullable = false, length = 1000)
    private String name;

    private String scheduleType;
    private String cycleCd;
    private String cycleValue;

    @Column(name = "status", nullable = false, updatable = false)
    private String status;

    private LocalDate scheduleStartAt;
    private LocalDate scheduleEndAt;

    @Column(updatable = false)
    private LocalDateTime createdAt;
    private LocalDateTime lastUpdatedAt;

    @Column(name = "created_by", nullable = false)
    private String createdBy;

    public Schedule toDomain() {
        return Schedule.fromEntity(this);
    }


    @PreUpdate
    public void preUpdate() {
        this.lastUpdatedAt = LocalDateTime.now();
    }

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
}
