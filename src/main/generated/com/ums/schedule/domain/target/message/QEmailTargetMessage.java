package com.ums.schedule.domain.target.message;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QEmailTargetMessage is a Querydsl query type for EmailTargetMessage
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QEmailTargetMessage extends EntityPathBase<EmailTargetMessage> {

    private static final long serialVersionUID = 855933611L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QEmailTargetMessage emailTargetMessage = new QEmailTargetMessage("emailTargetMessage");

    public final com.ums.schedule.domain.target.QTargetMessage _super;

    public final StringPath attachments = createString("attachments");

    public final StringPath bodyMessage = createString("bodyMessage");

    public final StringPath footerMessage = createString("footerMessage");

    public final StringPath headerMessage = createString("headerMessage");

    public final ComparablePath<java.util.UUID> id = createComparable("id", java.util.UUID.class);

    public final com.ums.schedule.domain.message.email.QEmailSendMessage sendMessage;

    // inherited
    public final com.ums.schedule.domain.target.QSendTarget sendTarget;

    public final StringPath subject = createString("subject");

    public QEmailTargetMessage(String variable) {
        this(EmailTargetMessage.class, forVariable(variable), INITS);
    }

    public QEmailTargetMessage(Path<? extends EmailTargetMessage> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QEmailTargetMessage(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QEmailTargetMessage(PathMetadata metadata, PathInits inits) {
        this(EmailTargetMessage.class, metadata, inits);
    }

    public QEmailTargetMessage(Class<? extends EmailTargetMessage> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this._super = new com.ums.schedule.domain.target.QTargetMessage(type, metadata, inits);
        this.sendMessage = inits.isInitialized("sendMessage") ? new com.ums.schedule.domain.message.email.QEmailSendMessage(forProperty("sendMessage"), inits.get("sendMessage")) : null;
        this.sendTarget = _super.sendTarget;
    }

}

