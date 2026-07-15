package com.ums.schedule.adapter.api.email.smtp;

import com.ums.schedule.adapter.api.email.smtp.response.SmtpSessionInfo;
import com.ums.schedule.application.send.mime.MimeWriter;
import com.ums.schedule.domain.send.email.mime.MimeMessage;
import com.ums.schedule.adapter.api.email.smtp.response.EmailSmtpResponse;
import com.ums.schedule.common.code.email.SmtpCommandType;
import com.ums.schedule.domain.send.email.job.DomainGroupTarget;
import com.ums.schedule.domain.send.email.job.EmailSendJob;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.ConnectException;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class EmailSmtpClient {
    private final SmtpHelper helper;
    private final MimeWriter writer;

    public List<EmailSmtpResponse> send(EmailSendJob job, String domain, List<DomainGroupTarget> targetList) {
        int retryCount = 0;
        List<EmailSmtpResponse> response = new ArrayList<>();
        SmtpSessionInfo session = null;
        try {
            session = helper.createSession(domain); // CONNECTION 및 ELHO 명령어 질의
            session.writeMailFrom(job.mailFrom());

            for (DomainGroupTarget target : targetList) {
                try {
                    session.writeReceiver(target.receiverEmail());
                    MimeMessage message = MimeMessage.of(job, target);
                    writer.write(session, message);
                } catch (Exception e) {

                } finally {

                }
            }

            session.sendCommand(SmtpCommandType.QUIT);
        } catch (ConnectException e) {
            if(retryCount == 3) {
                throw new RuntimeException(e);
            }
            retryCount++;
        } catch (IOException e) {
            // 700 error
            throw new RuntimeException(e);
        } finally {
            if(session != null) {
                session.close();
                session = null;
            }
        }
        return null;
    }
}
