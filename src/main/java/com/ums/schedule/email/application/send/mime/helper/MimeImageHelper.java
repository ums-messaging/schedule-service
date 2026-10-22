package com.ums.schedule.email.application.send.mime.helper;

import com.ums.schedule.email.application.send.mime.model.MimeMessage;
import com.ums.schedule.email.application.send.mime.writer.MimeWriter;
import com.ums.schedule.email.application.send.mime.model.MultiPartImage;
import com.ums.schedule.common.util.TemplateUtil;
import com.ums.schedule.email.domain.target.SendTargetRequest;
import com.ums.schedule.orchestrator.adaptor.out.storage.AwsS3FileMetadataResponse;
import com.ums.schedule.orchestrator.adaptor.out.storage.AwsS3Repository;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.*;

@Slf4j
@Component(value = "mimeImageWriter")
public class MimeImageHelper extends MimeMessageWriteHelper {
    private final AwsS3Repository repository;

    public MimeImageHelper(@Qualifier("mimeHtmlWriter") MimeWriteHelper writer, AwsS3Repository repository) {
        super(writer);
        this.repository = repository;
    }

    @Override
    public Writer write(MimeWriter writer, SendTargetRequest target) throws IOException, TemplateException {
        MimeMessage mimeMessage = writer.mimeMessage();
        if (mimeMessage.images().size() > 0) {
            mimeMessage.setImageHeader(writer);
            super.write(writer, target);

            for (MultiPartImage image : mimeMessage.images()) {
                log.info("image.info = {}",image);
                String fileKey = TemplateUtil.parse(image.src(), target.getMessageVariable());
                AwsS3FileMetadataResponse fileMetadata = repository.getFileMetadata(fileKey);
                String contentType = fileMetadata.contentType();
                image.setHeader(writer, contentType, fileKey);
                InputStream is = repository.getFileContent(fileKey);
                writer.write(is);
            }
            mimeMessage.setImageFooter(writer);
            return writer.writer();
        } else {
            return super.write(writer, target);
        }
    }
}
