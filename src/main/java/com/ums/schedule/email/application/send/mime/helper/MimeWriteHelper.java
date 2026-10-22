package com.ums.schedule.email.application.send.mime.helper;

import com.ums.schedule.email.application.send.mime.writer.MimeWriter;
import com.ums.schedule.email.domain.target.SendTargetRequest;
import freemarker.template.TemplateException;

import java.io.IOException;
import java.io.Writer;

public interface MimeWriteHelper {
    Writer write(MimeWriter writer, SendTargetRequest target) throws IOException, TemplateException;
}
