package com.ums.schedule.orchestrator.adaptor.in.config;

import com.ums.schedule.orchestrator.adaptor.in.config.mapper.SendRequestUploadConfigMapper;
import com.ums.schedule.orchestrator.adaptor.in.config.properties.SendRequestUploadProperties;
import com.ums.schedule.orchestrator.application.upload.config.SendRequestUploadConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SendRequestUploadConfigProvider {
    private final SendRequestUploadConfigMapper mapper;
    private final SendRequestUploadProperties properties;

    public SendRequestUploadConfig load() {
        return mapper.toUploadConfig(properties);
    }

    public Long getFileLimitSize() {
        return properties.getFileLimitSize();
    }

}
