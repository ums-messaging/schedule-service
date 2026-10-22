package com.ums.schedule.orchestrator.application.channel;

import com.ums.schedule.common.code.mapper.EnumMapperValue;
import com.ums.schedule.orchestrator.adaptor.out.api.message.ChannelMessageClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ChannelClientFactory {
    private final List<ChannelMessageClient> clients;

    public ChannelMessageClient findChannelClient(EnumMapperValue channelType) {
        return clients.stream()
                .filter(v -> v.supports(channelType))
                .findFirst()
                .orElseThrow();
    }
}
