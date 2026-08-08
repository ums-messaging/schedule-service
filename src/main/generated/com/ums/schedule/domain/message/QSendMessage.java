package com.ums.schedule.domain.message;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QSendMessage is a Querydsl query type for SendMessage
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QSendMessage extends EntityPathBase<SendMessage> {

    private static final long serialVersionUID = -1618317723L;

    public static final QSendMessage sendMessage = new QSendMessage("sendMessage");

    public final ComparablePath<java.util.UUID> id = createComparable("id", java.util.UUID.class);

    public final StringPath messagePrefix = createString("messagePrefix");

    public final EnumPath<com.ums.schedule.common.code.message.MessageType> messageType = createEnum("messageType", com.ums.schedule.common.code.message.MessageType.class);

    public final StringPath templateKey = createString("templateKey");

    public QSendMessage(String variable) {
        super(SendMessage.class, forVariable(variable));
    }

    public QSendMessage(Path<? extends SendMessage> path) {
        super(path.getType(), path.getMetadata());
    }

    public QSendMessage(PathMetadata metadata) {
        super(SendMessage.class, metadata);
    }

}

