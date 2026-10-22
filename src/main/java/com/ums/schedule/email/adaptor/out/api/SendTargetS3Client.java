package com.ums.schedule.email.adaptor.out.api;

import com.ums.schedule.email.application.target.reader.SendTargetFileReader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SendTargetS3Client implements SendTargetClient {
    private final SendTargetFileReader reader;

}
