package com.ums.schedule.domain.message;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;
import com.ums.schedule.domain.sendrequest.resource.email.EmailAttachment;
import com.ums.schedule.domain.sendrequest.message.email.EmailSendMessage;


/**
 * QEmailSendMessage is a Querydsl query type for EmailSendMessage
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QEmailSendMessage extends EntityPathBase<EmailSendMessage> {

    private static final long serialVersionUID = -1523530019L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QEmailSendMessage emailSendMessage = new QEmailSendMessage("emailSendMessage");

    public final ListPath<EmailAttachment, com.ums.schedule.domain.email.QEmailAttachment> attachmentList = this.<EmailAttachment, com.ums.schedule.domain.email.QEmailAttachment>createList("attachmentList", EmailAttachment.class, com.ums.schedule.domain.email.QEmailAttachment.class, PathInits.DIRECT2);

    public final StringPath bodyTemplate = createString("bodyTemplate");

    public final StringPath bodyTemplateKey = createString("bodyTemplateKey");

    public final StringPath footerTemplate = createString("footerTemplate");

    public final StringPath footerTemplateKey = createString("footerTemplateKey");

    public final StringPath headerTemplate = createString("headerTemplate");

    public final StringPath headerTemplateKey = createString("headerTemplateKey");

    public final ComparablePath<java.util.UUID> id = createComparable("id", java.util.UUID.class);

    public final StringPath imageDir = createString("imageDir");

    public final QSendMessage sendMessage;

    public final StringPath subject = createString("subject");

    public QEmailSendMessage(String variable) {
        this(EmailSendMessage.class, forVariable(variable), INITS);
    }

    public QEmailSendMessage(Path<? extends EmailSendMessage> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QEmailSendMessage(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QEmailSendMessage(PathMetadata metadata, PathInits inits) {
        this(EmailSendMessage.class, metadata, inits);
    }

    public QEmailSendMessage(Class<? extends EmailSendMessage> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.sendMessage = inits.isInitialized("sendMessage") ? new QSendMessage(forProperty("sendMessage"), inits.get("sendMessage")) : null;
    }

}

