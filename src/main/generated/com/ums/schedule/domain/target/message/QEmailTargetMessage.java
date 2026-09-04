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

    //inherited
    public final NumberPath<Integer> attemptNo;

    public final StringPath bodyMessage = createString("bodyMessage");

    //inherited
    public final StringPath contact;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt;

    public final StringPath domain = createString("domain");

    public final StringPath footerMessage = createString("footerMessage");

    //inherited
    public final NumberPath<Long> groupId;

    public final StringPath headerMessage = createString("headerMessage");

    //inherited
    public final NumberPath<Long> id;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastUploadedAt;

    //inherited
    public final StringPath messageVariable;

    //inherited
    public final StringPath resultMessage;

    public final com.ums.schedule.domain.message.email.QEmailSendMessage sendMessage;

    //inherited
    public final SimplePath<com.ums.schedule.domain.target.state.SendTargetState> state;

    public final StringPath subject = createString("subject");

    //inherited
    public final StringPath targetKey;

    //inherited
    public final StringPath targetName;

    // inherited
    public final com.ums.schedule.domain.target.upload.QTargetUploadReport targetUploadReport;

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
        this.attemptNo = _super.attemptNo;
        this.contact = _super.contact;
        this.createdAt = _super.createdAt;
        this.groupId = _super.groupId;
        this.id = _super.id;
        this.lastUploadedAt = _super.lastUploadedAt;
        this.messageVariable = _super.messageVariable;
        this.resultMessage = _super.resultMessage;
        this.sendMessage = inits.isInitialized("sendMessage") ? new com.ums.schedule.domain.message.email.QEmailSendMessage(forProperty("sendMessage"), inits.get("sendMessage")) : null;
        this.state = _super.state;
        this.targetKey = _super.targetKey;
        this.targetName = _super.targetName;
        this.targetUploadReport = _super.targetUploadReport;
    }

}

