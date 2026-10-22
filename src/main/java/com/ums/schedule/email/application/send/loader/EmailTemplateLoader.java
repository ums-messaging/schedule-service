package com.ums.schedule.email.application.send.loader;

import com.ums.schedule.email.application.send.mime.model.MultiPartImage;
import com.ums.schedule.email.application.send.job.model.MailBodyMessage;
import com.ums.schedule.email.application.send.job.model.MessageSendData;
import com.ums.schedule.email.application.send.mime.model.MimeAttachFile;
import com.ums.schedule.email.application.send.loader.model.TemplateLoadResult;
import com.ums.schedule.orchestrator.adaptor.out.storage.AwsS3Repository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;


@Slf4j
@Component
@RequiredArgsConstructor
public class EmailTemplateLoader {
    private final AwsS3Repository fileRepository;
    private final AttachFileLoader loader;
    private final ImageLoader imageLoader;

    public TemplateLoadResult load(Path file, MessageSendData sendData) throws IOException {
        MailBodyMessage bodyMessage = sendData.bodyMessage();
        List<MultiPartImage> images = new ArrayList<>();

        try (BufferedWriter writer = Files.newBufferedWriter(file, StandardCharsets.UTF_8)) {
                images.addAll(write(bodyMessage.header(), writer, images));
                images.addAll(write(bodyMessage.body(), writer, images));
                images.addAll(write(bodyMessage.footer(), writer, images));
            }
            List<MimeAttachFile> files = loader.load(sendData.attachMessages());
            return TemplateLoadResult.of(
                    file,
                    images,
                    files
            );
    }

    private List<MultiPartImage> write(String fileKey, BufferedWriter writer, List<MultiPartImage> images) throws IOException {
        List<MultiPartImage> files = new ArrayList<>();
        if(existsFile(fileKey)) {
            try (InputStream is = fileRepository.getFileContent(fileKey)) {
                Document document = Jsoup.parse(is, StandardCharsets.UTF_8.name(), "");
                files = imageLoader.load(document);
                writer.write(document.body().html());
                writer.write("\r\n");
            }
        }
        return files;
    }

    private boolean existsFile(String templateKey) {
        if(!hasTemplateKey(templateKey)) {
            return false;
        }
        return fileRepository.existsFile(templateKey);
    }

    private boolean hasTemplateKey(String templateKey) {
        return StringUtils.hasText(templateKey);
    }
}
