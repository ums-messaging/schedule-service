package com.ums.schedule.email.application.send.job.config;

import com.ums.schedule.email.adaptor.in.config.properties.SendJobProperties;

public record SendJobConfig(
        Integer batchSize,
        Integer partitionSize
) {
    public static SendJobConfig from(SendJobProperties properties) {
        return new SendJobConfig(
                properties.getBatchSize(),
                properties.getPartitionSize()
        );
    }
}
