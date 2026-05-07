package com.ums.schedule.domain.state;

import com.ums.schedule.code.EnumMapperType;

public interface StatusState {
    StatusState onEvent(StatusStateEvent event);
    EnumMapperType getCurrentCode();
}
