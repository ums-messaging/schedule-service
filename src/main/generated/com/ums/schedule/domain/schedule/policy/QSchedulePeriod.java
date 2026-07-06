package com.ums.schedule.domain.schedule.policy;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QSchedulePeriod is a Querydsl query type for SchedulePeriod
 */
@Generated("com.querydsl.codegen.DefaultEmbeddableSerializer")
public class QSchedulePeriod extends BeanPath<SchedulePeriod> {

    private static final long serialVersionUID = 1586630352L;

    public static final QSchedulePeriod schedulePeriod = new QSchedulePeriod("schedulePeriod");

    public final DatePath<java.time.LocalDate> scheduleEndAt = createDate("scheduleEndAt", java.time.LocalDate.class);

    public final DatePath<java.time.LocalDate> scheduleStartAt = createDate("scheduleStartAt", java.time.LocalDate.class);

    public QSchedulePeriod(String variable) {
        super(SchedulePeriod.class, forVariable(variable));
    }

    public QSchedulePeriod(Path<? extends SchedulePeriod> path) {
        super(path.getType(), path.getMetadata());
    }

    public QSchedulePeriod(PathMetadata metadata) {
        super(SchedulePeriod.class, metadata);
    }

}

