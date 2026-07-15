package com.ums.schedule.domain.message.email.attachment;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;
import com.ums.schedule.common.code.email.ConvertType;


/**
 * QEmailAttachment is a Querydsl query type for EmailAttachment
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QEmailAttachment extends EntityPathBase<EmailAttachment> {

    private static final long serialVersionUID = 894458144L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QEmailAttachment emailAttachment = new QEmailAttachment("emailAttachment");

    public final StringPath attachmentName = createString("attachmentName");

    public final EnumPath<ConvertType> convertType = createEnum("convertType", ConvertType.class);

    public final StringPath downloadName = createString("downloadName");

    public final StringPath fileKey = createString("fileKey");

    public final StringPath fileKeyTemplate = createString("fileKeyTemplate");

    public final NumberPath<Long> fileSize = createNumber("fileSize", Long.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.ums.schedule.domain.message.email.QSecurityMailPolicy securityPolicy;

    public final com.ums.schedule.domain.message.email.QEmailSendMessage sendMessage;

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
        this.securityPolicy = inits.isInitialized("securityPolicy") ? new com.ums.schedule.domain.message.email.QSecurityMailPolicy(forProperty("securityPolicy")) : null;
        this.sendMessage = inits.isInitialized("sendMessage") ? new com.ums.schedule.domain.message.email.QEmailSendMessage(forProperty("sendMessage"), inits.get("sendMessage")) : null;
    }

}

