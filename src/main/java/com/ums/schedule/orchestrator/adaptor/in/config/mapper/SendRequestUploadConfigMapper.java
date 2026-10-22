package com.ums.schedule.orchestrator.adaptor.in.config.mapper;

import com.ums.schedule.orchestrator.adaptor.in.config.properties.SendRequestUploadProperties;
import com.ums.schedule.orchestrator.application.upload.config.SendRequestUploadConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SendRequestUploadConfigMapper {

    public SendRequestUploadConfig toUploadConfig(SendRequestUploadProperties properties) {
        log.debug("send.request.upload = {}", properties.toString());
        return new SendRequestUploadConfig(properties.getBucket(),
                    properties.getDir()
        );
    }
}
