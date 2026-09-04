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

    public final NumberPath<Integer> attemptNo = createNumber("attemptNo", Integer.class);

    public final StringPath contact = createString("contact");

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final NumberPath<Long> groupId = createNumber("groupId", Long.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final DateTimePath<java.time.LocalDateTime> lastUploadedAt = createDateTime("lastUploadedAt", java.time.LocalDateTime.class);

    public final StringPath messageVariable = createString("messageVariable");

    public final StringPath resultMessage = createString("resultMessage");

    public final SimplePath<com.ums.schedule.domain.target.state.SendTargetState> state = createSimple("state", com.ums.schedule.domain.target.state.SendTargetState.class);

    public final StringPath targetKey = createString("targetKey");

    public final StringPath targetName = createString("targetName");

    public final com.ums.schedule.domain.target.upload.QTargetUploadReport targetUploadReport;

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
        this.targetUploadReport = inits.isInitialized("targetUploadReport") ? new com.ums.schedule.domain.target.upload.QTargetUploadReport(forProperty("targetUploadReport"), inits.get("targetUploadReport")) : null;
    }

}

