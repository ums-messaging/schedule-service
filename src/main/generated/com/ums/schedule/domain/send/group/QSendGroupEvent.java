package com.ums.schedule.domain.send.group;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QSendGroupEvent is a Querydsl query type for SendGroupEvent
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QSendGroupEvent extends EntityPathBase<SendGroupEvent> {

    private static final long serialVersionUID = -1094911003L;

    public static final QSendGroupEvent sendGroupEvent = new QSendGroupEvent("sendGroupEvent");

    public final NumberPath<Long> eventId = createNumber("eventId", Long.class);

    public final EnumPath<com.ums.schedule.common.code.request.SendGroupEventType> eventType = createEnum("eventType", com.ums.schedule.common.code.request.SendGroupEventType.class);

    public final NumberPath<Long> failCount = createNumber("failCount", Long.class);

    public final NumberPath<Long> groupId = createNumber("groupId", Long.class);

    public final DateTimePath<java.time.LocalDateTime> issuedAt = createDateTime("issuedAt", java.time.LocalDateTime.class);

    public final StringPath payload = createString("payload");

    public final EnumPath<com.ums.schedule.common.code.email.EmailResultCode> resultCode = createEnum("resultCode", com.ums.schedule.common.code.email.EmailResultCode.class);

    public final StringPath resultMessage = createString("resultMessage");

    public final NumberPath<Long> sendRequestId = createNumber("sendRequestId", Long.class);

    public final NumberPath<Long> successCount = createNumber("successCount", Long.class);

    public final NumberPath<Long> totalCount = createNumber("totalCount", Long.class);

    public QSendGroupEvent(String variable) {
        super(SendGroupEvent.class, forVariable(variable));
    }

    public QSendGroupEvent(Path<? extends SendGroupEvent> path) {
        super(path.getType(), path.getMetadata());
    }

    public QSendGroupEvent(PathMetadata metadata) {
        super(SendGroupEvent.class, metadata);
    }

}

