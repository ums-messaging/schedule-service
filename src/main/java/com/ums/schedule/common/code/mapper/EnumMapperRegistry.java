package com.ums.schedule.common.code.mapper;

import com.ums.schedule.domain.message.email.code.AttachmentEnumMapper;
import com.ums.schedule.domain.send.email.code.EmailEnumMapper;
import com.ums.schedule.domain.message.email.code.SecurityMailEnumMapper;
import com.ums.schedule.domain.sendrequest.template.code.TemplateEnumMapper;
import com.ums.schedule.domain.sendrequest.code.SendRequestEnumMapper;
import com.ums.schedule.domain.schedule.code.ScheduleEnumMapper;
import com.ums.schedule.domain.sendrequest.target.upload.code.TargetUploadReportEnumMapper;
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
