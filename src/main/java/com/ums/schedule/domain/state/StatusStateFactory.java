package com.ums.schedule.domain.state;

import com.ums.schedule.code.EnumMapperType;

public interface StatusStateFactory extends EnumMapperType {
    StatusState createStatus();
}
