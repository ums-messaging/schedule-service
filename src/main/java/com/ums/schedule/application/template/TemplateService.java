package com.ums.schedule.application.template;

import com.ums.schedule.domain.channel.ChannelTemplate;

public interface TemplateService {
    ChannelTemplate assemble(Long requestId, String templateKey);
}
