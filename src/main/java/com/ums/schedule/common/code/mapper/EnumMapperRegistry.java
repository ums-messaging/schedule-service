package com.ums.schedule.common.code.mapper;

import com.ums.schedule.common.code.email.EmailEnumMapper;
import com.ums.schedule.common.code.email.security.SecurityMailEnumMapper;
import com.ums.schedule.common.code.message.MessageEnumMapper;
import com.ums.schedule.common.code.message.MessageType;
import com.ums.schedule.common.code.request.SendRequestEnumMapper;
import com.ums.schedule.common.code.schedule.ScheduleEnumMapper;
import com.ums.schedule.common.code.target.TargetEnumMapper;
import com.ums.schedule.common.code.target_upload.TargetUploadReportEnumMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EnumMapperRegistry {
    @Autowired
    public EnumMapperRegistry(EnumMapperFactory factory) {
        factory.register(ScheduleEnumMapper.class);
        factory.register(SendRequestEnumMapper.class);
        factory.register(SecurityMailEnumMapper.class);
        factory.register(TargetUploadReportEnumMapper.class);
        factory.register(TargetEnumMapper.class);
        factory.register(EmailEnumMapper.class);
        factory.register(MessageEnumMapper.class);
    }
}
