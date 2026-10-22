package com.ums.schedule.email.application.send.mime.helper;

import com.ums.schedule.email.application.send.mime.writer.MimeWriter;
import com.ums.schedule.email.domain.target.SendTargetRequest;
import freemarker.template.TemplateException;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.io.Writer;

@RequiredArgsConstructor
public abstract class MimeMessageWriteHelper implements MimeWriteHelper {
    private final MimeWriteHelper writer;

    @Override
    public Writer write(MimeWriter w, SendTargetRequest target)  throws IOException, TemplateException {
        return writer.write(w, target);
    }
}
