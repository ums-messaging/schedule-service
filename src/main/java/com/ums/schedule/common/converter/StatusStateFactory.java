package com.ums.schedule.common.converter;

import com.ums.schedule.common.code.mapper.EnumMapperType;

public interface StatusStateFactory extends EnumMapperType {
    StatusState createStatus();
}
