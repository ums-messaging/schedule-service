package com.ums.schedule.email.adaptor.out.persistence.jpa.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QSendTargetGroupEventEntity is a Querydsl query type for SendTargetGroupEventEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QSendTargetGroupEventEntity extends EntityPathBase<SendTargetGroupEventEntity> {

    private static final long serialVersionUID = -1157030610L;

    public static final QSendTargetGroupEventEntity sendTargetGroupEventEntity = new QSendTargetGroupEventEntity("sendTargetGroupEventEntity");

    public final StringPath eventType = createString("eventType");

    public final NumberPath<Integer> failCount = createNumber("failCount", Integer.class);

    public final NumberPath<Long> groupId = createNumber("groupId", Long.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final DateTimePath<java.time.LocalDateTime> issuedAt = createDateTime("issuedAt", java.time.LocalDateTime.class);

    public final StringPath resultCode = createString("resultCode");

    public final StringPath resultMessage = createString("resultMessage");

    public final NumberPath<Long> sendRequestId = createNumber("sendRequestId", Long.class);

    public final NumberPath<Integer> successCount = createNumber("successCount", Integer.class);

    public final NumberPath<Integer> totalCount = createNumber("totalCount", Integer.class);

    public QSendTargetGroupEventEntity(String variable) {
        super(SendTargetGroupEventEntity.class, forVariable(variable));
    }

    public QSendTargetGroupEventEntity(Path<? extends SendTargetGroupEventEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QSendTargetGroupEventEntity(PathMetadata metadata) {
        super(SendTargetGroupEventEntity.class, metadata);
    }

}

