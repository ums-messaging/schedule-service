package com.ums.schedule.application.target.uploader;

import com.ums.schedule.application.sendrequest.target.result.TargetUploadResultList;
import com.ums.schedule.application.target.exception.TargetUploadReportNotFoundException;
import com.ums.schedule.application.ums.common.target.context.SendTargetGroupedList;
import com.ums.schedule.application.ums.email.exception.EmailMessageNotFoundException;
import com.ums.schedule.application.target.uploader.model.EmailGeneratorContext;
import com.ums.schedule.application.ums.common.template.loader.model.EmailTemplate;
import com.ums.schedule.application.ums.common.template.loader.EmailTemplateLoader;
import com.ums.schedule.common.code.common.ChannelType;
import com.ums.schedule.common.code.mapper.EnumMapperValue;
import com.ums.schedule.domain.message.email.EmailSendMessage;
import com.ums.schedule.domain.message.email.EmailSendMessageJpaRepository;
import com.ums.schedule.domain.target.upload.TargetUploadReport;
import com.ums.schedule.domain.target.upload.TargetUploadReportJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

@Slf4j
@Component
@RequiredArgsConstructor
public class EmailTargetUploader implements TargetUploader {
    private final TargetUploadReportJpaRepository targetUploadReportRepository;
    private final EmailSendMessageJpaRepository messageRepository;
    private final EmailTemplateLoader templateLoader;
    private final EmailTargetUploadWorker worker;

    @Override
    public boolean supports(EnumMapperValue mapperValue) {
        return ChannelType.EMAIL == ChannelType.valueOf(mapperValue.code());
    }

    @Transactional
    @Override
    public Consumer<List<SendTargetGroupedList>> upload(UUID reportId, UUID messageId) {
        log.info("[{}] target.upload.start.", reportId.toString());

        TargetUploadReport targetUploadReport = targetUploadReportRepository.findById(reportId)
                .orElseThrow(()-> TargetUploadReportNotFoundException.of(reportId));
        EmailSendMessage sendMessage = messageRepository.findById(messageId)
                .orElseThrow(() -> EmailMessageNotFoundException.of(messageId.toString()));
        EmailTemplate template = templateLoader.loadTemplate(sendMessage);
        EmailGeneratorContext context = EmailGeneratorContext.of(targetUploadReport, template, sendMessage);

        Consumer<List<SendTargetGroupedList>> consumer = rows -> {
            long startTime = System.currentTimeMillis();
            List<CompletableFuture<TargetUploadResultList>> futures = worker.process(context, List.copyOf(rows));


            List<TargetUploadResultList> results = futures.stream().
                    map(CompletableFuture::join).toList();

            printAndStatistics(results);
            long endTime = System.currentTimeMillis();
            log.info("duration.time = {}", endTime - startTime);
        };
        return consumer;
    }

    private void printAndStatistics(List<TargetUploadResultList> results) {
        int succeedCount = results.stream()
                .mapToInt(TargetUploadResultList::succeedCount)
                .sum();
        int failedCount = results.stream()
                .mapToInt(TargetUploadResultList::failedCount)
                .sum();
        log.info("succeedCount = {}, failedCount = {}", succeedCount, failedCount);
    }
}
