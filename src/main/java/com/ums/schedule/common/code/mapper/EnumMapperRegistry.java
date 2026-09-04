package com.ums.schedule.common.code.mapper;

import com.ums.schedule.common.code.email.EmailCode;
import com.ums.schedule.common.code.email.security.SecurityMailCode;
import com.ums.schedule.common.code.message.MessageCode;
import com.ums.schedule.common.code.request.SendRequestCode;
import com.ums.schedule.common.code.schedule.ScheduleCode;
import com.ums.schedule.common.code.target.SendTargetCode;
import com.ums.schedule.common.code.target_upload.TargetUploadReportCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EnumMapperRegistry {
    @Autowired
    public EnumMapperRegistry(EnumMapperFactory factory) {
        factory.register(ScheduleCode.class);
        factory.register(SendRequestCode.class);
        factory.register(SecurityMailCode.class);
        factory.register(TargetUploadReportCode.class);
        factory.register(SendTargetCode.class);
        factory.register(EmailCode.class);
        factory.register(MessageCode.class);
    }
}
