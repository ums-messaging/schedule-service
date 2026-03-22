package com.ums.schedule.domain;

import com.ums.schedule.common.code.RestrictModeEnum;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.Optional;

@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(name = "uq_schedule_restrict_mode",
                columnNames = {"schedule_id", "restrict_mode"})
})
public class ScheduleRestrictPolicy {
    @Id
    @Getter
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "restrict_policy_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "restrict_mode", nullable = false)
    private RestrictModeEnum restrictMode;

    @ManyToOne
    @Getter
    @JoinColumn(name = "schedule_id", nullable = false)
    private Schedule schedule;

    public void applySchedule(Schedule schedule) {
        this.schedule = schedule;
        this.schedule.getRestrictPolicies().add(this);
    }
}
