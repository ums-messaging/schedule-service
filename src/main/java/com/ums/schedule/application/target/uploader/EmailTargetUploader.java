package com.ums.schedule.application.target.uploader;

import com.ums.schedule.application.sendrequest.target.SendTargetUploadService;
import com.ums.schedule.application.sendrequest.target.result.SendTargetSaveResult;
import com.ums.schedule.application.target.exception.TargetUploadReportNotConfiguredException;
import com.ums.schedule.application.target.reader.model.TargetRowResult;
import com.ums.schedule.application.ums.common.target.context.SendTargetCreateContext;
import com.ums.schedule.application.ums.common.target.context.SendTargetGroupedListContext;
import com.ums.schedule.application.ums.common.target.result.TargetMessageResult;
import com.ums.schedule.application.ums.email.exception.EmailMessageNotFoundException;
import com.ums.schedule.application.ums.email.generator.EmailTargetMessageGenerator;
import com.ums.schedule.application.target.uploader.model.EmailGeneratorContext;
import com.ums.schedule.application.ums.common.template.loader.model.EmailTemplate;
import com.ums.schedule.application.ums.common.template.loader.EmailTemplateLoader;
import com.ums.schedule.common.code.common.ChannelType;
import com.ums.schedule.common.code.mapper.EnumMapperValue;
import com.ums.schedule.common.code.target_upload.TargetUploadConfiguration;
import com.ums.schedule.config.properties.TargetUploadProperties;
import com.ums.schedule.domain.message.email.EmailSendMessage;
import com.ums.schedule.domain.message.email.EmailSendMessageJpaRepository;
import com.ums.schedule.domain.target.SendTarget;
import com.ums.schedule.domain.target.TargetMessage;
import com.ums.schedule.domain.target.upload.TargetUploadReport;
import com.ums.schedule.domain.target.upload.TargetUploadReportJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.function.Consumer;

@Slf4j
@Component
public class EmailTargetUploader implements TargetUploader {
    private final EmailSendMessageJpaRepository messageRepository;
    private final EmailTemplateLoader templateLoader;
    private final SendTargetUploadService targetUploadService;
    private final EmailTargetMessageGenerator generator;
    private final ThreadPoolTaskExecutor executor;

    @Autowired
    public EmailTargetUploader(
                                EmailSendMessageJpaRepository messageRepository,
                                EmailTemplateLoader templateLoader,
                                SendTargetUploadService targetUploadService,
                                EmailTargetMessageGenerator generator,
                                @Qualifier("targetUploadExecutor") ThreadPoolTaskExecutor executor
                               ) {
        this.messageRepository = messageRepository;
        this.targetUploadService = targetUploadService;
        this.templateLoader = templateLoader;
        this.generator = generator;
        this.executor = executor;
    }

    @Override
    public boolean supports(EnumMapperValue mapperValue) {
        return ChannelType.EMAIL == ChannelType.valueOf(mapperValue.code());
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public Consumer<List<SendTargetGroupedListContext>> upload(TargetUploadReport report, UUID messageId) {
        log.info("[{}] target.upload.start.", report.getId().toString());

        EmailSendMessage sendMessage = messageRepository.findById(messageId)
                .orElseThrow(() -> EmailMessageNotFoundException.of(messageId.toString()));
        EmailTemplate template = templateLoader.loadTemplate(sendMessage);

        EmailGeneratorContext context = EmailGeneratorContext.of(report, template, sendMessage);

        Consumer<List<SendTargetGroupedListContext>> consumer = rows -> {
            List<CompletableFuture<SendTargetSaveResult>> futures = new ArrayList<>();
            List<SendTargetGroupedListContext> snapshot = List.copyOf(rows); // 20개
            log.info("snapshot.size = {}", snapshot.size());
            long startTime = System.currentTimeMillis();

            for (SendTargetGroupedListContext row : snapshot) {
                CompletableFuture<SendTargetSaveResult> future = CompletableFuture.supplyAsync(() -> {
                    printLog();
                    List<TargetRowResult> targetRows = List.copyOf(row.targetGroupedList());
                    List<TargetMessageResult> results = targetRows.stream()
                            .map(r -> generator.generate(row.partitionNo(), context, r))
                            .toList();
                    SendTargetSaveResult result = targetUploadService.upload(report, results);

                    return result;
                }, executor);

                futures.add(future);
            }

            CompletableFuture.allOf(
                            futures.stream()
                                    .toArray(CompletableFuture[]::new))
                    .join();

            List<SendTargetSaveResult> results = futures.stream().
                    map(CompletableFuture::join).toList();

            int succeedCount = results.stream()
                    .mapToInt(SendTargetSaveResult::succeedCount)
                    .sum();
            int failedCount = results.stream()
                    .mapToInt(SendTargetSaveResult::failedCount)
                    .sum();

            log.info("succeedCount = {}, failedCount = {}", succeedCount, failedCount);
            long endTime = System.currentTimeMillis();
            log.info("duration.time = {}", endTime - startTime);
        };

        return consumer;
    }

    private void printLog() {
        ThreadPoolExecutor threadPool = executor.getThreadPoolExecutor();
        Thread thread = Thread.currentThread();
        log.info(
                "targetUploadExecutor - threadName= {}. poolSize={}, activeCount={}, corePoolSize={}, maxPoolSize={}, queueSize={}, queueRemainingCapacity={}, completedTaskCount={}, taskCount={}",
                thread.getName(),
                threadPool.getPoolSize(),
                threadPool.getActiveCount(),
                threadPool.getCorePoolSize(),
                threadPool.getMaximumPoolSize(),
                threadPool.getQueue().size(),
                threadPool.getQueue().remainingCapacity(),
                threadPool.getCompletedTaskCount(),
                threadPool.getTaskCount()
        );
    }
}
