package com.ums.schedule.domain.sendrequest.target;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QSendTarget is a Querydsl query type for SendTarget
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QSendTarget extends EntityPathBase<SendTarget> {

    private static final long serialVersionUID = 96205308L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QSendTarget sendTarget = new QSendTarget("sendTarget");

    public final NumberPath<Integer> attemptNo = createNumber("attemptNo", Integer.class);

    public final StringPath contact = createString("contact");

    public final StringPath content = createString("content");

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final ComparablePath<java.util.UUID> id = createComparable("id", java.util.UUID.class);

    public final DateTimePath<java.time.LocalDateTime> lastUploadedAt = createDateTime("lastUploadedAt", java.time.LocalDateTime.class);

    public final StringPath messageVariable = createString("messageVariable");

    public final StringPath resourceJson = createString("resourceJson");

    public final EnumPath<com.ums.schedule.domain.sendrequest.target.code.SendTargetStatusEnum> status = createEnum("status", com.ums.schedule.domain.sendrequest.target.code.SendTargetStatusEnum.class);

    public final StringPath targetKey = createString("targetKey");

    public final StringPath targetName = createString("targetName");

    public final com.ums.schedule.domain.sendrequest.target.upload.QTargetUploadReport targetUpload;

    public final StringPath title = createString("title");

    public QSendTarget(String variable) {
        this(SendTarget.class, forVariable(variable), INITS);
    }

    public QSendTarget(Path<? extends SendTarget> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QSendTarget(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QSendTarget(PathMetadata metadata, PathInits inits) {
        this(SendTarget.class, metadata, inits);
    }

    public QSendTarget(Class<? extends SendTarget> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.targetUpload = inits.isInitialized("targetUpload") ? new com.ums.schedule.domain.sendrequest.target.upload.QTargetUploadReport(forProperty("targetUpload"), inits.get("targetUpload")) : null;
    }

}

