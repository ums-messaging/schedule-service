package com.ums.schedule.domain.sendrequest;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;
import com.ums.schedule.domain.send.report.SendRequestReport;


/**
 * QSendRequestReport is a Querydsl query type for SendRequestReport
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QSendRequestReport extends EntityPathBase<SendRequestReport> {

    private static final long serialVersionUID = -2029265655L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QSendRequestReport sendRequestReport = new QSendRequestReport("sendRequestReport");

    public final DateTimePath<java.time.LocalDateTime> aggregatedAt = createDateTime("aggregatedAt", java.time.LocalDateTime.class);

    public final StringPath downloadUrl = createString("downloadUrl");

    public final NumberPath<Long> failCount = createNumber("failCount", Long.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Long> sendingCount = createNumber("sendingCount", Long.class);

    public final QSendRequest sendRequest;

    public final NumberPath<Long> successCount = createNumber("successCount", Long.class);

    public final NumberPath<Long> totalCount = createNumber("totalCount", Long.class);

    public QSendRequestReport(String variable) {
        this(SendRequestReport.class, forVariable(variable), INITS);
    }

    public QSendRequestReport(Path<? extends SendRequestReport> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QSendRequestReport(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QSendRequestReport(PathMetadata metadata, PathInits inits) {
        this(SendRequestReport.class, metadata, inits);
    }

    public QSendRequestReport(Class<? extends SendRequestReport> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.sendRequest = inits.isInitialized("sendRequest") ? new QSendRequest(forProperty("sendRequest"), inits.get("sendRequest")) : null;
    }

}

