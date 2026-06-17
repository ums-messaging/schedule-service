package com.ums.schedule.domain.sendrequest;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QSendRequest is a Querydsl query type for SendRequest
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QSendRequest extends EntityPathBase<SendRequest> {

    private static final long serialVersionUID = -980236427L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QSendRequest sendRequest = new QSendRequest("sendRequest");

    public final EnumPath<com.ums.schedule.domain.sendrequest.code.ChannelTypeEnum> channelType = createEnum("channelType", com.ums.schedule.domain.sendrequest.code.ChannelTypeEnum.class);

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final com.ums.schedule.domain.sendrequest.upload.QTargetUploadReport currentTargetUpload;

    public final com.ums.schedule.domain.sendrequest.customer.QCustomerRequestKey customerRequestKey;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final DateTimePath<java.time.LocalDateTime> requestedAt = createDateTime("requestedAt", java.time.LocalDateTime.class);

    public final NumberPath<Integer> retryCnt = createNumber("retryCnt", Integer.class);

    public final com.ums.schedule.domain.schedule.QSchedule schedule;

    public final DateTimePath<java.time.LocalDateTime> sendCompletedAt = createDateTime("sendCompletedAt", java.time.LocalDateTime.class);

    public final StringPath senderKey = createString("senderKey");

    public final DateTimePath<java.time.LocalDateTime> sendStartedAt = createDateTime("sendStartedAt", java.time.LocalDateTime.class);

    public final SimplePath<com.ums.schedule.domain.sendrequest.state.SendRequestState> state = createSimple("state", com.ums.schedule.domain.sendrequest.state.SendRequestState.class);

    public final StringPath templateKey = createString("templateKey");

    public QSendRequest(String variable) {
        this(SendRequest.class, forVariable(variable), INITS);
    }

    public QSendRequest(Path<? extends SendRequest> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QSendRequest(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QSendRequest(PathMetadata metadata, PathInits inits) {
        this(SendRequest.class, metadata, inits);
    }

    public QSendRequest(Class<? extends SendRequest> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.currentTargetUpload = inits.isInitialized("currentTargetUpload") ? new com.ums.schedule.domain.sendrequest.upload.QTargetUploadReport(forProperty("currentTargetUpload"), inits.get("currentTargetUpload")) : null;
        this.customerRequestKey = inits.isInitialized("customerRequestKey") ? new com.ums.schedule.domain.sendrequest.customer.QCustomerRequestKey(forProperty("customerRequestKey")) : null;
        this.schedule = inits.isInitialized("schedule") ? new com.ums.schedule.domain.schedule.QSchedule(forProperty("schedule"), inits.get("schedule")) : null;
    }

}

