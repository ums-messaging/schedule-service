package com.ums.schedule.email.domain.requests.state;

import com.ums.schedule.email.common.code.EmailSendStatusCode;

public class EmailSendRequestCreateState implements EmailSendRequestState {

    @Override
    public EmailSendStatusCode currentStateCode() {
        return EmailSendStatusCode.CREATE;
    }
}
