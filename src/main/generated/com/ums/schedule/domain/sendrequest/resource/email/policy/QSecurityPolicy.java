package com.ums.schedule.domain.sendrequest.resource.email.policy;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QSecurityPolicy is a Querydsl query type for SecurityPolicy
 */
@Generated("com.querydsl.codegen.DefaultEmbeddableSerializer")
public class QSecurityPolicy extends BeanPath<SecurityPolicy> {

    private static final long serialVersionUID = 1248865878L;

    public static final QSecurityPolicy securityPolicy = new QSecurityPolicy("securityPolicy");

    public final EnumPath<com.ums.schedule.domain.sendrequest.resource.email.code.EncryptionTypeEnum> encryptionType = createEnum("encryptionType", com.ums.schedule.domain.sendrequest.resource.email.code.EncryptionTypeEnum.class);

    public final StringPath passwordFormat = createString("passwordFormat");

    public final EnumPath<com.ums.schedule.domain.sendrequest.resource.email.code.PasswordHashEnum> passwordHash = createEnum("passwordHash", com.ums.schedule.domain.sendrequest.resource.email.code.PasswordHashEnum.class);

    public final StringPath passwordPolicy = createString("passwordPolicy");

    public final EnumPath<com.ums.schedule.domain.sendrequest.resource.email.code.PermissionMaskEnum> permissionMask = createEnum("permissionMask", com.ums.schedule.domain.sendrequest.resource.email.code.PermissionMaskEnum.class);

    public QSecurityPolicy(String variable) {
        super(SecurityPolicy.class, forVariable(variable));
    }

    public QSecurityPolicy(Path<? extends SecurityPolicy> path) {
        super(path.getType(), path.getMetadata());
    }

    public QSecurityPolicy(PathMetadata metadata) {
        super(SecurityPolicy.class, metadata);
    }

}

