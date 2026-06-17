package com.ums.schedule.domain.sendrequest.message;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QSendMessage is a Querydsl query type for SendMessage
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QSendMessage extends EntityPathBase<SendMessage> {

    private static final long serialVersionUID = 556161150L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QSendMessage sendMessage = new QSendMessage("sendMessage");

    public final ComparablePath<java.util.UUID> id = createComparable("id", java.util.UUID.class);

    public final StringPath messagePrefix = createString("messagePrefix");

    public final com.ums.schedule.domain.sendrequest.QSendRequest sendRequest;

    public final EnumPath<com.ums.schedule.domain.sendrequest.template.code.TemplateTypeEnum> templateType = createEnum("templateType", com.ums.schedule.domain.sendrequest.template.code.TemplateTypeEnum.class);

    public QSendMessage(String variable) {
        this(SendMessage.class, forVariable(variable), INITS);
    }

    public QSendMessage(Path<? extends SendMessage> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QSendMessage(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QSendMessage(PathMetadata metadata, PathInits inits) {
        this(SendMessage.class, metadata, inits);
    }

    public QSendMessage(Class<? extends SendMessage> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.sendRequest = inits.isInitialized("sendRequest") ? new com.ums.schedule.domain.sendrequest.QSendRequest(forProperty("sendRequest"), inits.get("sendRequest")) : null;
    }

}

