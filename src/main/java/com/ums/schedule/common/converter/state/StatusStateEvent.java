package com.ums.schedule.common.converter.state;

import com.ums.schedule.common.code.mapper.EnumMapperType;

public interface StatusStateEvent extends EnumMapperType {
    StatusStateType stateType();
}
