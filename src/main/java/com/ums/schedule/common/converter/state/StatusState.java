package com.ums.schedule.common.converter.state;

import com.ums.schedule.common.code.mapper.EnumMapperType;

public interface StatusState {
    StatusState onEvent(StatusStateEvent event);
    EnumMapperType getCurrentCode();
}
