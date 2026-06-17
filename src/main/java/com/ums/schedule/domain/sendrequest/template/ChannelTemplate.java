package com.ums.schedule.domain.sendrequest.template;


import com.ums.schedule.domain.sendrequest.target.SendTarget;

public interface ChannelTemplate {
    String compile(SendTarget target);
    String getTitle(SendTarget target);
}
