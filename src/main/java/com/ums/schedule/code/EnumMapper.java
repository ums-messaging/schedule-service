package com.ums.schedule.code;

import com.ums.schedule.code.EnumMapperType;

public interface EnumMapper {
    String key();
    Class<? extends EnumMapperType> code();
}
