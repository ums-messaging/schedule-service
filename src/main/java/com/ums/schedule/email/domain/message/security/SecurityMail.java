package com.ums.schedule.email.domain.message.security;

import com.ums.schedule.common.code.email.EmailType;
import com.ums.schedule.common.code.email.security.*;
import com.ums.schedule.common.code.mapper.EnumMapperValue;

import com.ums.schedule.email.adaptor.out.persistence.jpa.entity.EmailSendMessageEntity;

public record SecurityMail (
        EncryptionTypeEnum encryptionType,
        PasswordHashEnum passwdHash,
        PermissionMaskEnum permissionMask,
        PasswordType passwdType
) {

    public static SecurityMail of(EncryptionTypeEnum encryptionType,
                                  PasswordHashEnum passwdHash,
                                  PermissionMaskEnum permissionMask,
                                  PasswordType passwdType) {
        return new SecurityMail(
                encryptionType,
                passwdHash,
                permissionMask,
                passwdType
        );
    }


    public static SecurityMail fromEntity(EmailType type, EmailSendMessageEntity entity) {
        if(EmailType.SECURITY == type) {
            return new SecurityMail(
                EncryptionTypeEnum.valueOf(entity.getEncryptionType()),
                    PasswordHashEnum.valueOf(entity.getPasswdHash()),
                    PermissionMaskEnum.valueOf(entity.getPermissionMask()),
                PasswordType.valueOf(entity.getPasswdType())
            );
        }
        return null;
    }

}
