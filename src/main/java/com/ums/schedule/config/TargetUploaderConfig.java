package com.ums.schedule.config;

import com.ums.schedule.application.sendrequest.target.processor.FileTargetUploadProcessor;
import com.ums.schedule.application.sendrequest.target.processor.JsonTargetUploadProcessor;
import com.ums.schedule.application.sendrequest.target.processor.TargetUploadProcessor;
import com.ums.schedule.domain.sendrequest.target.upload.code.TargetUploadTypeEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class TargetUploaderConfig {
    private final FileTargetUploadProcessor fileTargetUploader;
    private final JsonTargetUploadProcessor jsonTargetUploader;

    @Bean
    public Map<TargetUploadTypeEnum, TargetUploadProcessor> targetUploaderMap() {
        return Map.of(
                TargetUploadTypeEnum.FILE, fileTargetUploader,
                TargetUploadTypeEnum.JSON, jsonTargetUploader
        );
    }
}
