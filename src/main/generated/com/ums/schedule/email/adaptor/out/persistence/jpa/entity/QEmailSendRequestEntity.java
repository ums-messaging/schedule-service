package com.ums.schedule.email.adaptor.out.persistence.jpa.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QEmailSendRequestEntity is a Querydsl query type for EmailSendRequestEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QEmailSendRequestEntity extends EntityPathBase<EmailSendRequestEntity> {

    private static final long serialVersionUID = -2106725211L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QEmailSendRequestEntity emailSendRequestEntity = new QEmailSendRequestEntity("emailSendRequestEntity");

    public final QBaseEntity _super = new QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final StringPath from = createString("from");

    public final StringPath fromName = createString("fromName");

    public final NumberPath<Integer> groupCount = createNumber("groupCount", Integer.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastUpdatedAt = _super.lastUpdatedAt;

    public final QEmailSendMessageEntity message;

    public final NumberPath<Integer> requestFailCount = createNumber("requestFailCount", Integer.class);

    public final NumberPath<Long> requestId = createNumber("requestId", Long.class);

    public final NumberPath<Integer> requestSuccessCount = createNumber("requestSuccessCount", Integer.class);

    public final StringPath resultCode = createString("resultCode");

    public final StringPath resultMessage = createString("resultMessage");

    public final DateTimePath<java.time.LocalDateTime> sendEndedAt = createDateTime("sendEndedAt", java.time.LocalDateTime.class);

    public final NumberPath<Integer> sendFailCount = createNumber("sendFailCount", Integer.class);

    public final DateTimePath<java.time.LocalDateTime> sendRequestedAt = createDateTime("sendRequestedAt", java.time.LocalDateTime.class);

    public final DateTimePath<java.time.LocalDateTime> sendStartedAt = createDateTime("sendStartedAt", java.time.LocalDateTime.class);

    public final NumberPath<Integer> sendSuccessCount = createNumber("sendSuccessCount", Integer.class);

    public final StringPath status = createString("status");

    public final NumberPath<Integer> totalCount = createNumber("totalCount", Integer.class);

    public QEmailSendRequestEntity(String variable) {
        this(EmailSendRequestEntity.class, forVariable(variable), INITS);
    }

    public QEmailSendRequestEntity(Path<? extends EmailSendRequestEntity> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QEmailSendRequestEntity(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QEmailSendRequestEntity(PathMetadata metadata, PathInits inits) {
        this(EmailSendRequestEntity.class, metadata, inits);
    }

    public QEmailSendRequestEntity(Class<? extends EmailSendRequestEntity> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.message = inits.isInitialized("message") ? new QEmailSendMessageEntity(forProperty("message"), inits.get("message")) : null;
    }

}

