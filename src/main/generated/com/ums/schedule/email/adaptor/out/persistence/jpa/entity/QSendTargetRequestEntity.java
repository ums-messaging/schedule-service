package com.ums.schedule.email.adaptor.out.persistence.jpa.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QSendTargetRequestEntity is a Querydsl query type for SendTargetRequestEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QSendTargetRequestEntity extends EntityPathBase<SendTargetRequestEntity> {

    private static final long serialVersionUID = 1337875266L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QSendTargetRequestEntity sendTargetRequestEntity = new QSendTargetRequestEntity("sendTargetRequestEntity");

    public final QBaseEntity _super = new QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final StringPath email = createString("email");

    public final NumberPath<Long> groupId = createNumber("groupId", Long.class);

    public final StringPath groupKey = createString("groupKey");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastUpdatedAt = _super.lastUpdatedAt;

    public final StringPath messageVariable = createString("messageVariable");

    public final StringPath resultCode = createString("resultCode");

    public final StringPath resultMessage = createString("resultMessage");

    public final QEmailSendRequestEntity sendRequest;

    public final StringPath status = createString("status");

    public final StringPath targetKey = createString("targetKey");

    public final StringPath targetName = createString("targetName");

    public QSendTargetRequestEntity(String variable) {
        this(SendTargetRequestEntity.class, forVariable(variable), INITS);
    }

    public QSendTargetRequestEntity(Path<? extends SendTargetRequestEntity> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QSendTargetRequestEntity(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QSendTargetRequestEntity(PathMetadata metadata, PathInits inits) {
        this(SendTargetRequestEntity.class, metadata, inits);
    }

    public QSendTargetRequestEntity(Class<? extends SendTargetRequestEntity> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.sendRequest = inits.isInitialized("sendRequest") ? new QEmailSendRequestEntity(forProperty("sendRequest"), inits.get("sendRequest")) : null;
    }

}

