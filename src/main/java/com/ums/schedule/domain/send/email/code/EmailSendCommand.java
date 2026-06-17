package com.ums.schedule.domain.send.email.code;

import com.ums.schedule.common.code.mapper.EnumMapperType;

public enum EmailSendCommand implements EnumMapperType {
    CONNECT("220" , ""),
    EHLO("250", "EHLO ${target}"),
    MAIL_FROM("250", "MAIL FROM: <${target}>"),
    RCPT_TO("250", "RCPT TO: <${target}>"),
    DATA("321", "DATA"),
    END("250", "."),
    QUIT("221", "QUIT")
    ;

    String value;
    String description;

    EmailSendCommand (String value, String description) {
        this.value = value;
        this.description = description;
    }

    @Override
    public String code() {
        return this.name();
    }

    @Override
    public String value() {
        return this.value;
    }

    @Override
    public String description() {
        return this.description;
    }

    public boolean isSuccess(String line) {
        return line.startsWith(this.value);
    }

    public String toCommand(String to) {
        if(description.contains("${target}")) {
            return description.replace("${target}", to);
        }
        return description.concat(to);
    }
}
