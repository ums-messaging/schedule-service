package com.ums.schedule.common.code.mapper;

import com.ums.schedule.common.code.email.EmailCode;
import com.ums.schedule.common.code.email.security.SecurityMailCode;
import com.ums.schedule.common.code.message.MessageCode;
import com.ums.schedule.common.code.request.SendRequestEnumMapper;
import com.ums.schedule.common.code.schedule.ScheduleEnumMapper;
import com.ums.schedule.common.code.target.TargetEnumMapper;
import com.ums.schedule.common.code.target_upload.TargetUploadReportCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EnumMapperRegistry {
    @Autowired
    public EnumMapperRegistry(EnumMapperFactory factory) {
        factory.register(ScheduleEnumMapper.class);
        factory.register(SendRequestEnumMapper.class);
        factory.register(SecurityMailCode.class);
        factory.register(TargetUploadReportCode.class);
        factory.register(TargetEnumMapper.class);
        factory.register(EmailCode.class);
        factory.register(MessageCode.class);
    }
}
