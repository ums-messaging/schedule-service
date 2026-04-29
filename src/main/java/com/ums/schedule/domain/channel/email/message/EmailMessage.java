package com.ums.schedule.domain.channel.email.message;

import com.ums.schedule.domain.channel.ChannelMessage;

public record EmailMessage(
    EmailTitle title,
    EmailTemplate template,
    EmailBody body
) implements ChannelMessage {

}
