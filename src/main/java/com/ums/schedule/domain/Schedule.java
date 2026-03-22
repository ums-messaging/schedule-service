package com.ums.schedule.domain;

import com.ums.schedule.common.code.CycleCdEnum;
import com.ums.schedule.common.code.ScheduleStatusEnum;
import com.ums.schedule.common.code.ScheduleTypeEnum;
import com.ums.schedule.domain.cycle_policy.ScheduleCyclePolicy;
import com.ums.schedule.domain.schedule_status.ScheduleStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.GenerationType.IDENTITY;
import static jakarta.persistence.GenerationType.SEQUENCE;

@Entity
@Getter
public class Schedule {
    @Id
    @GeneratedValue(strategy = SEQUENCE)
    @Column(name = "schedule_id")
    private Long id;

    @Column(name = "schedule_name", nullable = false)
    private String name;

    @Column(name = "schedule_type", nullable = false)
    private ScheduleTypeEnum type;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private ScheduleStatusEnum status;

    @Embedded
    private SchedulePeriod schedulePeriod;

    @Getter
    @OneToMany(mappedBy = "schedule", cascade = { CascadeType.PERSIST})
    private List<ScheduleRestrictPolicy> restrictPolicies = new ArrayList<>();

    @Getter
    @OneToMany(mappedBy = "schedule", cascade = { CascadeType.ALL })
    private List<SendRequest> sendRequests = new ArrayList<>();
}
