package com.ums.schedule.email.adaptor.out.persistence.jpa.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QSendTargetErrorEntity is a Querydsl query type for SendTargetErrorEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QSendTargetErrorEntity extends EntityPathBase<SendTargetErrorEntity> {

    private static final long serialVersionUID = -1182648325L;

    public static final QSendTargetErrorEntity sendTargetErrorEntity = new QSendTargetErrorEntity("sendTargetErrorEntity");

    public final QBaseEntity _super = new QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final StringPath errorMessage = createString("errorMessage");

    public final StringPath errorType = createString("errorType");

    public final NumberPath<Long> groupId = createNumber("groupId", Long.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastUpdatedAt = _super.lastUpdatedAt;

    public final StringPath payload = createString("payload");

    public final NumberPath<Long> sendRequestId = createNumber("sendRequestId", Long.class);

    public final NumberPath<Long> targetId = createNumber("targetId", Long.class);

    public QSendTargetErrorEntity(String variable) {
        super(SendTargetErrorEntity.class, forVariable(variable));
    }

    public QSendTargetErrorEntity(Path<? extends SendTargetErrorEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QSendTargetErrorEntity(PathMetadata metadata) {
        super(SendTargetErrorEntity.class, metadata);
    }

}

