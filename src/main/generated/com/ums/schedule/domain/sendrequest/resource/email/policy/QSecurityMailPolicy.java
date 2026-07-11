package com.ums.schedule.domain.sendrequest.resource.email.policy;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.ums.schedule.domain.message.email.SecurityMailPolicy;
import com.ums.schedule.domain.message.email.code.PasswordHashEnum;
import com.ums.schedule.domain.message.email.code.PermissionMaskEnum;


/**
 * QSecurityMailPolicy is a Querydsl query type for SecurityMailPolicy
 */
@Generated("com.querydsl.codegen.DefaultEmbeddableSerializer")
public class QSecurityMailPolicy extends BeanPath<SecurityMailPolicy> {

    private static final long serialVersionUID = 1829996781L;

    public static final QSecurityMailPolicy securityMailPolicy = new QSecurityMailPolicy("securityMailPolicy");

    public final EnumPath<com.ums.schedule.domain.message.email.code.EncryptionTypeEnum> encryptionType = createEnum("encryptionType", com.ums.schedule.domain.message.email.code.EncryptionTypeEnum.class);

    public final StringPath passwordFormat = createString("passwordFormat");

    public final EnumPath<PasswordHashEnum> passwordHash = createEnum("passwordHash", PasswordHashEnum.class);

    public final StringPath passwordPolicy = createString("passwordPolicy");

    public final EnumPath<PermissionMaskEnum> permissionMask = createEnum("permissionMask", PermissionMaskEnum.class);

    public QSecurityMailPolicy(String variable) {
        super(SecurityMailPolicy.class, forVariable(variable));
    }

    public QSecurityMailPolicy(Path<? extends SecurityMailPolicy> path) {
        super(path.getType(), path.getMetadata());
    }

    public QSecurityMailPolicy(PathMetadata metadata) {
        super(SecurityMailPolicy.class, metadata);
    }

}

