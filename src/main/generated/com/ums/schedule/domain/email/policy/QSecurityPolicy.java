package com.ums.schedule.domain.email.policy;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.ums.schedule.domain.sendrequest.resource.email.code.EncryptionTypeEnum;
import com.ums.schedule.domain.sendrequest.resource.email.code.PasswordHashEnum;
import com.ums.schedule.domain.sendrequest.resource.email.code.PermissionMaskEnum;
import com.ums.schedule.domain.sendrequest.resource.email.policy.SecurityPolicy;


/**
 * QSecurityPolicy is a Querydsl query type for SecurityPolicy
 */
@Generated("com.querydsl.codegen.DefaultEmbeddableSerializer")
public class QSecurityPolicy extends BeanPath<SecurityPolicy> {

    private static final long serialVersionUID = 268028329L;

    public static final QSecurityPolicy securityPolicy = new QSecurityPolicy("securityPolicy");

    public final EnumPath<EncryptionTypeEnum> encryptionType = createEnum("encryptionType", EncryptionTypeEnum.class);

    public final StringPath passwordFormat = createString("passwordFormat");

    public final EnumPath<PasswordHashEnum> passwordHash = createEnum("passwordHash", PasswordHashEnum.class);

    public final StringPath passwordPolicy = createString("passwordPolicy");

    public final EnumPath<PermissionMaskEnum> permissionMask = createEnum("permissionMask", PermissionMaskEnum.class);

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

