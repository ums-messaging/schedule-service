package com.ums.schedule.attachment.application.handler;

import com.ums.schedule.attachment.domain.Attachment;
import com.ums.schedule.send.application.model.dto.SendTargetDto;
import freemarker.template.Template;

import java.io.File;
import java.io.IOException;

/**
 * 이메일 바디 기준으로 해야함
 *
 * */
// target정보 세팅하면서
public interface EmailBodyHandler {
    File handle(Attachment attachment, Template template, SendTargetDto targetDto) throws IOException;
}
