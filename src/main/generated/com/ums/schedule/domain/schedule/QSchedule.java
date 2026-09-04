package com.ums.schedule.domain.schedule;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QSchedule is a Querydsl query type for Schedule
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QSchedule extends EntityPathBase<Schedule> {

    private static final long serialVersionUID = -2046053893L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QSchedule schedule = new QSchedule("schedule");

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final StringPath createdBy = createString("createdBy");

    public final com.ums.schedule.domain.schedule.policy.cycle.QScheduleCyclePolicy cyclePolicy;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final DateTimePath<java.time.LocalDateTime> lastUpdatedAt = createDateTime("lastUpdatedAt", java.time.LocalDateTime.class);

    public final StringPath name = createString("name");

    public final com.ums.schedule.domain.schedule.policy.QSchedulePeriod schedulePeriod;

    public final SimplePath<com.ums.schedule.domain.schedule.state.ScheduleStatus> status = createSimple("status", com.ums.schedule.domain.schedule.state.ScheduleStatus.class);

    public QSchedule(String variable) {
        this(Schedule.class, forVariable(variable), INITS);
    }

    public QSchedule(Path<? extends Schedule> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QSchedule(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QSchedule(PathMetadata metadata, PathInits inits) {
        this(Schedule.class, metadata, inits);
    }

    public QSchedule(Class<? extends Schedule> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.cyclePolicy = inits.isInitialized("cyclePolicy") ? new com.ums.schedule.domain.schedule.policy.cycle.QScheduleCyclePolicy(forProperty("cyclePolicy")) : null;
        this.schedulePeriod = inits.isInitialized("schedulePeriod") ? new com.ums.schedule.domain.schedule.policy.QSchedulePeriod(forProperty("schedulePeriod")) : null;
    }

}

