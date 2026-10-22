package com.ums.schedule.email.application.target.reader.factory;

import com.ums.schedule.common.code.mapper.EnumMapperValue;
import com.ums.schedule.email.application.target.reader.SendTargetReader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class SendTargetReaderFactory {
    private final List<SendTargetReader> readers;

    public SendTargetReader getSendTargetReader(EnumMapperValue uploadType) {
        SendTargetReader reader = readers.stream()
                .filter(v -> v.supports(uploadType))
                .findFirst()
                .orElseThrow();
        return reader;
    }
}
