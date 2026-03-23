package com.ums.schedule.common.code;

import com.ums.schedule.attachment.code.AttachmentEnumMapper;
import com.ums.schedule.message.code.EmailMessageEnumMapper;
import com.ums.schedule.schedule.code.ScheduleEnumMapper;
import com.ums.schedule.template.domain.code.TemplateEnumMapper;
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
