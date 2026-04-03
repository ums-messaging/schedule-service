package com.ums.schedule.send.config;

import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
public class TargetReaderConfig {
    public S3Client s3Client() {
        return S3Client.builder()
                .region(Region.AP_NORTHEAST_2)
                .credentialsProvider(ProfileCredentialsProvider.create())
                .build();
    }
}
