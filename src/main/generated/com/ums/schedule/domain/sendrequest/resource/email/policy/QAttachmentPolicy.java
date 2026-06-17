package com.ums.schedule.domain.sendrequest.resource.email.policy;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QAttachmentPolicy is a Querydsl query type for AttachmentPolicy
 */
@Generated("com.querydsl.codegen.DefaultEmbeddableSerializer")
public class QAttachmentPolicy extends BeanPath<AttachmentPolicy> {

    private static final long serialVersionUID = -301958919L;

    public static final QAttachmentPolicy attachmentPolicy = new QAttachmentPolicy("attachmentPolicy");

    public final StringPath attachmentName = createString("attachmentName");

    public final StringPath downloadName = createString("downloadName");

    public QAttachmentPolicy(String variable) {
        super(AttachmentPolicy.class, forVariable(variable));
    }

    public QAttachmentPolicy(Path<? extends AttachmentPolicy> path) {
        super(path.getType(), path.getMetadata());
    }

    public QAttachmentPolicy(PathMetadata metadata) {
        super(AttachmentPolicy.class, metadata);
    }

}

