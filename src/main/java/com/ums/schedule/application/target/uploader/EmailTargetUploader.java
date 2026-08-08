package com.ums.schedule.application.target.uploader;

import com.ums.schedule.application.sendrequest.target.SendTargetUploadService;
import com.ums.schedule.application.target.exception.TargetUploadReportNotConfiguredException;
import com.ums.schedule.application.target.reader.model.TargetRowResult;
import com.ums.schedule.application.ums.email.exception.EmailMessageNotFoundException;
import com.ums.schedule.application.ums.email.generator.EmailTargetMessageGenerator;
import com.ums.schedule.application.target.uploader.model.TargetUploadContext;
import com.ums.schedule.application.ums.common.template.loader.model.EmailTemplate;
import com.ums.schedule.application.ums.common.template.loader.EmailTemplateLoader;
import com.ums.schedule.common.code.common.ChannelType;
import com.ums.schedule.common.code.mapper.EnumMapperValue;
import com.ums.schedule.common.code.target_upload.TargetUploadConfiguration;
import com.ums.schedule.config.properties.TargetUploadProperties;
import com.ums.schedule.domain.message.email.EmailSendMessage;
import com.ums.schedule.domain.message.email.EmailSendMessageJpaRepository;
import com.ums.schedule.domain.target.SendTarget;
import com.ums.schedule.domain.target.upload.TargetUploadReport;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

@Component
@RequiredArgsConstructor
public class EmailTargetUploader implements TargetUploader {
    private final TargetUploadProperties properties;
    private final EmailSendMessageJpaRepository messageRepository;
    private final EmailTemplateLoader templateLoader;
    private final SendTargetUploadService targetUploadService;
    private final EmailTargetMessageGenerator generator;

    @Override
    public boolean supports(EnumMapperValue mapperValue) {
        return ChannelType.EMAIL == ChannelType.valueOf(mapperValue.code());
    }

    @Override
    public Consumer<List<TargetRowResult>> upload(TargetUploadReport report, UUID messageId) {
        EmailSendMessage sendMessage = messageRepository.findById(messageId)
                .orElseThrow(() -> EmailMessageNotFoundException.of(messageId.toString()));
        EmailTemplate template = templateLoader.loadTemplate(sendMessage);
        report.startTargetUpload();

        Integer partitionsSize = getPartitionSize();
        TargetUploadContext context = TargetUploadContext.of(report.getId(), template, sendMessage);

        Consumer<List<TargetRowResult>> consumer = rows -> {
            List<SendTarget> targetList = rows.stream()
                    .map(row -> generator.generate(context, row))
                    .toList();

            targetUploadService.upload(targetList, partitionsSize);
        };

        return consumer;
    }

    private Integer getPartitionSize() {
        Integer size = properties.getPartitionSize();
        if(size == 0) {
            throw TargetUploadReportNotConfiguredException.of(TargetUploadConfiguration.PARTITION_SIZE);
        }
        return size;
    }
}
