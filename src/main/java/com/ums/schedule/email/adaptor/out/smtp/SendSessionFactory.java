package com.ums.schedule.email.adaptor.out.smtp;

import com.ums.schedule.email.adaptor.out.smtp.model.SmtpSessionInfo;

public interface SendSessionFactory {
    SmtpSessionInfo createSession(String domain);

}
