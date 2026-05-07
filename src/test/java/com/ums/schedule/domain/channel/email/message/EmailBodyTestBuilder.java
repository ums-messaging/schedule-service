package com.ums.schedule.domain.channel.email.message;

import com.ums.schedule.code.email.ConvertTypeEnum;

public class EmailBodyTestBuilder {
    private ConvertTypeEnum convertType;

    public static EmailBodyTestBuilder builder() {
        return new EmailBodyTestBuilder();
    }

    public EmailBodyTestBuilder convertType(ConvertTypeEnum convertType) {
        this.convertType = convertType;
        return this;
    }

    public EmailBody build() {
        return new EmailBody(ConvertTypeEnum.NONE, null, null);
    }

}
