package com.ums.schedule.code;

import com.ums.schedule.code.email.AttachmentEnumMapper;
import com.ums.schedule.code.email.TemplateEnumMapper;
import com.ums.schedule.code.schedule.ScheduleEnumMapper;
import com.ums.schedule.code.send.EmailMessageEnumMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EnumMapperRegistry {
    @Autowired
    public EnumMapperRegistry(EnumMapperFactory factory) {
        factory.register(TemplateEnumMapper.class);
        factory.register(EmailMessageEnumMapper.class);
        factory.register(AttachmentEnumMapper.class);
        factory.register(ScheduleEnumMapper.class);
    }
}
