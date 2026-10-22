package com.ums.schedule.email.domain.event;

import java.util.List;

public interface SendTargetGroupEventRepository {
    void saveAll(List<SendTargetGroupEvent> eventList);
    void save(SendTargetGroupEvent event);
}
