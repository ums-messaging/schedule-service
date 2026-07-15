package com.ums.schedule.domain.schedule.policy.cycle;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.ums.schedule.common.code.schedule.CycleCd;
import com.ums.schedule.common.code.schedule.ScheduleType;


/**
 * QScheduleCyclePolicy is a Querydsl query type for ScheduleCyclePolicy
 */
@Generated("com.querydsl.codegen.DefaultEmbeddableSerializer")
public class QScheduleCyclePolicy extends BeanPath<ScheduleCyclePolicy> {

    private static final long serialVersionUID = -41941247L;

    public static final QScheduleCyclePolicy scheduleCyclePolicy = new QScheduleCyclePolicy("scheduleCyclePolicy");

    public final EnumPath<CycleCd> cycleCd = createEnum("cycleCd", CycleCd.class);

    public final StringPath cycleValue = createString("cycleValue");

    public final EnumPath<ScheduleType> scheduleType = createEnum("scheduleType", ScheduleType.class);

    public QScheduleCyclePolicy(String variable) {
        super(ScheduleCyclePolicy.class, forVariable(variable));
    }

    public QScheduleCyclePolicy(Path<? extends ScheduleCyclePolicy> path) {
        super(path.getType(), path.getMetadata());
    }

    public QScheduleCyclePolicy(PathMetadata metadata) {
        super(ScheduleCyclePolicy.class, metadata);
    }

}

