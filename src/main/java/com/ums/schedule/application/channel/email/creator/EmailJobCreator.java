package com.ums.schedule.application.channel.email.creator;

import com.ums.schedule.application.channel.JobCreator;
import com.ums.schedule.application.channel.SendJob;
import com.ums.schedule.code.EnumMapperValue;
import com.ums.schedule.code.send.ChannelTypeEnum;
import com.ums.schedule.domain.request.SendRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmailJobCreator implements JobCreator {

    @Override
    public boolean supports(EnumMapperValue mapperValue) {
        return mapperValue.code().equals(ChannelTypeEnum.EMAIL.code());
    }

    @Override
    public SendJob createJob(SendRequest request) {
        return null;
    }
}
