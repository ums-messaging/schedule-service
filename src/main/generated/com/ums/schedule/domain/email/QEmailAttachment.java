package com.ums.schedule.domain.email;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;
import com.ums.schedule.domain.sendrequest.resource.email.EmailAttachment;
import com.ums.schedule.domain.sendrequest.resource.email.code.ConvertTypeEnum;


/**
 * QEmailAttachment is a Querydsl query type for EmailAttachment
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QEmailAttachment extends EntityPathBase<EmailAttachment> {

    private static final long serialVersionUID = -1495364934L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QEmailAttachment emailAttachment = new QEmailAttachment("emailAttachment");

    public final com.ums.schedule.domain.email.policy.QAttachmentPolicy attachmentPolicy;

    public final EnumPath<ConvertTypeEnum> convertType = createEnum("convertType", ConvertTypeEnum.class);

    public final StringPath fileKey = createString("fileKey");

    public final StringPath fileKeyTemplate = createString("fileKeyTemplate");

    public final NumberPath<Long> fileSize = createNumber("fileSize", Long.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.ums.schedule.domain.email.policy.QSecurityPolicy securityPolicy;

    public final com.ums.schedule.domain.message.QEmailSendMessage sendMessage;

    public QEmailAttachment(String variable) {
        this(EmailAttachment.class, forVariable(variable), INITS);
    }

    public QEmailAttachment(Path<? extends EmailAttachment> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QEmailAttachment(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QEmailAttachment(PathMetadata metadata, PathInits inits) {
        this(EmailAttachment.class, metadata, inits);
    }

    public QEmailAttachment(Class<? extends EmailAttachment> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.attachmentPolicy = inits.isInitialized("attachmentPolicy") ? new com.ums.schedule.domain.email.policy.QAttachmentPolicy(forProperty("attachmentPolicy")) : null;
        this.securityPolicy = inits.isInitialized("securityPolicy") ? new com.ums.schedule.domain.email.policy.QSecurityPolicy(forProperty("securityPolicy")) : null;
        this.sendMessage = inits.isInitialized("sendMessage") ? new com.ums.schedule.domain.message.QEmailSendMessage(forProperty("sendMessage"), inits.get("sendMessage")) : null;
    }

}

