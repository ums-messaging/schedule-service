package com.ums.schedule.application.channel.email.template.loader;

import com.ums.schedule.code.EnumMapperSelector;
import com.ums.schedule.adapter.api.template.email.EmailContentResponse;
import freemarker.template.Template;

import java.io.IOException;

public interface EmailTemplateLoader extends EnumMapperSelector {
    Template loadTemplate(EmailContentResponse content) throws IOException;
}
