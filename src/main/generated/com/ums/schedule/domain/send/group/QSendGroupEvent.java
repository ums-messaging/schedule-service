package com.ums.schedule.domain.send.group;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;
import com.ums.schedule.common.code.email.EmailResultCode;
import com.ums.schedule.common.code.request.SendGroupEventTypeEnum;


/**
 * QSendGroupEvent is a Querydsl query type for SendGroupEvent
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QSendGroupEvent extends EntityPathBase<SendGroupEvent> {

    private static final long serialVersionUID = -1094911003L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QSendGroupEvent sendGroupEvent = new QSendGroupEvent("sendGroupEvent");

    public final NumberPath<Long> eventId = createNumber("eventId", Long.class);

    public final EnumPath<SendGroupEventTypeEnum> eventType = createEnum("eventType", SendGroupEventTypeEnum.class);

    public final DateTimePath<java.time.LocalDateTime> issuedAt = createDateTime("issuedAt", java.time.LocalDateTime.class);

    public final StringPath payload = createString("payload");

    public final EnumPath<EmailResultCode> resultCode = createEnum("resultCode", EmailResultCode.class);

    public final StringPath resultMessage = createString("resultMessage");

    public final com.ums.schedule.domain.request.QSendRequest sendRequest;

    public QSendGroupEvent(String variable) {
        this(SendGroupEvent.class, forVariable(variable), INITS);
    }

    public QSendGroupEvent(Path<? extends SendGroupEvent> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QSendGroupEvent(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QSendGroupEvent(PathMetadata metadata, PathInits inits) {
        this(SendGroupEvent.class, metadata, inits);
    }

    public QSendGroupEvent(Class<? extends SendGroupEvent> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.sendRequest = inits.isInitialized("sendRequest") ? new com.ums.schedule.domain.request.QSendRequest(forProperty("sendRequest"), inits.get("sendRequest")) : null;
    }

}

