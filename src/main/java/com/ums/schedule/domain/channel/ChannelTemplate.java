package com.ums.schedule.domain.channel;


import com.ums.schedule.domain.target.SendTarget;

public interface ChannelTemplate {
    String compile(SendTarget target);
    String getTitle(SendTarget target);
}
