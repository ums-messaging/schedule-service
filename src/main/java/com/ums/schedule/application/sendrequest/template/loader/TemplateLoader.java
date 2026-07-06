package com.ums.schedule.application.sendrequest.template.loader;

import com.ums.schedule.common.code.mapper.EnumMapperSelector;

public interface TemplateLoader extends EnumMapperSelector {
    String loadTemplate(String content);
}
