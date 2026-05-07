package com.ums.schedule.application.request.dto;

import com.ums.schedule.adapter.api.send.SendCreateRequest;
import com.ums.schedule.code.EnumMapper;
import com.ums.schedule.code.EnumMapperValue;
import com.ums.schedule.code.send.SendRequestEnumMapper;
import com.ums.schedule.domain.request.CustomerRequestKey;
import com.ums.schedule.domain.schedule.Schedule;

import java.util.Map;

public record SendRequestCommand(
        Schedule schedule,
        EnumMapperValue channel,
        EnumMapperValue uploadType,
        CustomerRequestKey customerRequestKey
) {
    public static SendRequestCommand of(Schedule schedule, CustomerRequestKey customerRequestKey, Map<EnumMapper, EnumMapperValue> mapperValueMap, SendCreateRequest request) {
        return new SendRequestCommand(schedule,
                mapperValueMap.get(SendRequestEnumMapper.CHANNEL_TYPE),
                mapperValueMap.get(SendRequestEnumMapper.TARGET_UPLOAD_TYPE),
                customerRequestKey
        );

    }


}
