package com.ums.schedule.email.domain.requests.state;

import com.ums.schedule.email.common.code.EmailSendStatusCode;

public interface EmailSendRequestState {
    EmailSendStatusCode currentStateCode();
}
