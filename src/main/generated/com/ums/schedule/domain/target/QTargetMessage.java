package com.ums.schedule.domain.target;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QTargetMessage is a Querydsl query type for TargetMessage
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QTargetMessage extends EntityPathBase<TargetMessage> {

    private static final long serialVersionUID = -2102793588L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QTargetMessage targetMessage = new QTargetMessage("targetMessage");

    public final ComparablePath<java.util.UUID> id = createComparable("id", java.util.UUID.class);

    public final QSendTarget sendTarget;

    public QTargetMessage(String variable) {
        this(TargetMessage.class, forVariable(variable), INITS);
    }

    public QTargetMessage(Path<? extends TargetMessage> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QTargetMessage(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QTargetMessage(PathMetadata metadata, PathInits inits) {
        this(TargetMessage.class, metadata, inits);
    }

    public QTargetMessage(Class<? extends TargetMessage> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.sendTarget = inits.isInitialized("sendTarget") ? new QSendTarget(forProperty("sendTarget"), inits.get("sendTarget")) : null;
    }

}

