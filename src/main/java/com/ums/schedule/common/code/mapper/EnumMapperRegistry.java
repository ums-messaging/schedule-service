package com.ums.schedule.common.code.mapper;

import com.ums.schedule.common.code.email.AttachmentEnumMapper;
import com.ums.schedule.domain.send.email.code.EmailEnumMapper;
import com.ums.schedule.common.code.email.SecurityMailEnumMapper;
import com.ums.schedule.common.code.template.TemplateEnumMapper;
import com.ums.schedule.common.code.request.SendRequestEnumMapper;
import com.ums.schedule.common.code.schedule.ScheduleEnumMapper;
import com.ums.schedule.common.code.target_upload.TargetUploadReportEnumMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EnumMapperRegistry {
    @Autowired
    public EnumMapperRegistry(EnumMapperFactory factory) {
        factory.register(ScheduleEnumMapper.class);
        factory.register(SendRequestEnumMapper.class);
        factory.register(TemplateEnumMapper.class);
        factory.register(AttachmentEnumMapper.class);
        factory.register(SecurityMailEnumMapper.class);
        factory.register(TargetUploadReportEnumMapper.class);
        factory.register(EmailEnumMapper.class);
    }
}
