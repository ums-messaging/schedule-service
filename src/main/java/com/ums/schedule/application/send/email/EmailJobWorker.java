package com.ums.schedule.application.send.email;

import com.ums.schedule.application.send.JobWorker;
import com.ums.schedule.common.code.common.ChannelType;
import com.ums.schedule.common.code.mapper.EnumMapperValue;
import com.ums.schedule.domain.target.TargetMessageJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmailJobWorker implements JobWorker {
    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public boolean supports(EnumMapperValue channelType) {
        return ChannelType.EMAIL == ChannelType.valueOf(channelType.code());
    }

    @Override
    public void work(String groupId) {
    }
}
