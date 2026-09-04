package com.ums.schedule.domain.message.email.convert;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QConvertMail is a Querydsl query type for ConvertMail
 */
@Generated("com.querydsl.codegen.DefaultEmbeddableSerializer")
public class QConvertMail extends BeanPath<ConvertMail> {

    private static final long serialVersionUID = -1146845821L;

    public static final QConvertMail convertMail = new QConvertMail("convertMail");

    public final StringPath attachmentName = createString("attachmentName");

    public final EnumPath<com.ums.schedule.common.code.email.ConvertType> convertType = createEnum("convertType", com.ums.schedule.common.code.email.ConvertType.class);

    public final StringPath downloadName = createString("downloadName");

    public final StringPath fileKeyTemplate = createString("fileKeyTemplate");

    public QConvertMail(String variable) {
        super(ConvertMail.class, forVariable(variable));
    }

    public QConvertMail(Path<? extends ConvertMail> path) {
        super(path.getType(), path.getMetadata());
    }

    public QConvertMail(PathMetadata metadata) {
        super(ConvertMail.class, metadata);
    }

}

