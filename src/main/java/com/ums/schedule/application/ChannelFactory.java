package com.ums.schedule.application;

import com.ums.schedule.application.target.dto.SendTargetDto;
import com.ums.schedule.domain.channel.ChannelTemplate;
import com.ums.schedule.domain.channel.email.message.EmailTemplate;
import com.ums.schedule.domain.target.SendTarget;
import com.ums.schedule.domain.target.upload.TargetUpload;

import java.util.List;

public interface ChannelFactory<T extends ChannelTemplate> {

    List<SendTarget> makeMessage(Long uploadId, T template, List<SendTargetDto> targetList);
    T getTemplate(Long requestId, String templateKey);
}
