package com.ums.schedule.domain.message.email;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QEmailSendMessage is a Querydsl query type for EmailSendMessage
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QEmailSendMessage extends EntityPathBase<EmailSendMessage> {

    private static final long serialVersionUID = 351874927L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QEmailSendMessage emailSendMessage = new QEmailSendMessage("emailSendMessage");

    public final ListPath<com.ums.schedule.domain.message.email.attachment.EmailAttachment, com.ums.schedule.domain.message.email.attachment.QEmailAttachment> attachments = this.<com.ums.schedule.domain.message.email.attachment.EmailAttachment, com.ums.schedule.domain.message.email.attachment.QEmailAttachment>createList("attachments", com.ums.schedule.domain.message.email.attachment.EmailAttachment.class, com.ums.schedule.domain.message.email.attachment.QEmailAttachment.class, PathInits.DIRECT2);

    public final StringPath bodyTemplateKey = createString("bodyTemplateKey");

    public final com.ums.schedule.domain.message.email.convert.QConvertMail convertMail;

    public final StringPath coverTemplateKey = createString("coverTemplateKey");

    public final EnumPath<com.ums.schedule.common.code.email.EmailType> emailType = createEnum("emailType", com.ums.schedule.common.code.email.EmailType.class);

    public final StringPath footerTemplateKey = createString("footerTemplateKey");

    public final StringPath headerTemplateKey = createString("headerTemplateKey");

    public final ComparablePath<java.util.UUID> id = createComparable("id", java.util.UUID.class);

    public final StringPath imageDir = createString("imageDir");

    public final com.ums.schedule.domain.message.email.security.QSecurityMailPolicy securityMail;

    public final com.ums.schedule.domain.message.QSendMessage sendMessage;

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
        this.convertMail = inits.isInitialized("convertMail") ? new com.ums.schedule.domain.message.email.convert.QConvertMail(forProperty("convertMail")) : null;
        this.securityMail = inits.isInitialized("securityMail") ? new com.ums.schedule.domain.message.email.security.QSecurityMailPolicy(forProperty("securityMail")) : null;
        this.sendMessage = inits.isInitialized("sendMessage") ? new com.ums.schedule.domain.message.QSendMessage(forProperty("sendMessage")) : null;
    }

}

