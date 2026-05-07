package com.ums.schedule.fixture;

import com.ums.schedule.domain.channel.ChannelTemplate;
import com.ums.schedule.domain.target.SendTarget;

public class FakeTemplate implements ChannelTemplate {

    @Override
    public String compile(SendTarget target) {
        return "content";
    }

    @Override
    public String getTitle(SendTarget target) {
        return "title";
    }
}
