package com.ums.schedule.domain.sendrequest.target.upload;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QTargetUploadReport is a Querydsl query type for TargetUploadReport
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QTargetUploadReport extends EntityPathBase<TargetUploadReport> {

    private static final long serialVersionUID = 95757040L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QTargetUploadReport targetUploadReport = new QTargetUploadReport("targetUploadReport");

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final StringPath downloadKey = createString("downloadKey");

    public final NumberPath<Long> failCount = createNumber("failCount", Long.class);

    public final NumberPath<Long> fileSize = createNumber("fileSize", Long.class);

    public final ComparablePath<java.util.UUID> id = createComparable("id", java.util.UUID.class);

    public final DateTimePath<java.time.LocalDateTime> requestedAt = createDateTime("requestedAt", java.time.LocalDateTime.class);

    public final StringPath resultMessage = createString("resultMessage");

    public final com.ums.schedule.domain.sendrequest.QSendRequest sendRequest;

    public final SimplePath<com.ums.schedule.domain.sendrequest.target.upload.state.TargetUploadState> state = createSimple("state", com.ums.schedule.domain.sendrequest.target.upload.state.TargetUploadState.class);

    public final NumberPath<Long> successCount = createNumber("successCount", Long.class);

    public final NumberPath<Long> totalCount = createNumber("totalCount", Long.class);

    public final DateTimePath<java.time.LocalDateTime> uploadedAt = createDateTime("uploadedAt", java.time.LocalDateTime.class);

    public final EnumPath<com.ums.schedule.domain.sendrequest.target.upload.code.TargetUploadFormatEnum> uploadFormat = createEnum("uploadFormat", com.ums.schedule.domain.sendrequest.target.upload.code.TargetUploadFormatEnum.class);

    public final StringPath uploadKey = createString("uploadKey");

    public final EnumPath<com.ums.schedule.domain.sendrequest.target.upload.code.TargetUploadTypeEnum> uploadType = createEnum("uploadType", com.ums.schedule.domain.sendrequest.target.upload.code.TargetUploadTypeEnum.class);

    public QTargetUploadReport(String variable) {
        this(TargetUploadReport.class, forVariable(variable), INITS);
    }

    public QTargetUploadReport(Path<? extends TargetUploadReport> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QTargetUploadReport(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QTargetUploadReport(PathMetadata metadata, PathInits inits) {
        this(TargetUploadReport.class, metadata, inits);
    }

    public QTargetUploadReport(Class<? extends TargetUploadReport> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.sendRequest = inits.isInitialized("sendRequest") ? new com.ums.schedule.domain.sendrequest.QSendRequest(forProperty("sendRequest"), inits.get("sendRequest")) : null;
    }

}

