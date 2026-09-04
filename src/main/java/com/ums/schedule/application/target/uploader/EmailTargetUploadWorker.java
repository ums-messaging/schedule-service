package com.ums.schedule.application.target.uploader;

import com.ums.schedule.application.sendrequest.target.SendTargetUploadService;
import com.ums.schedule.application.sendrequest.target.result.TargetUploadResultList;
import com.ums.schedule.application.target.reader.model.TargetUploadRowResult;
import com.ums.schedule.application.target.uploader.model.EmailGeneratorContext;
import com.ums.schedule.application.ums.common.target.context.SendTargetGroupedList;
import com.ums.schedule.application.ums.common.target.result.SendTargetResult;
import com.ums.schedule.application.ums.email.generator.EmailTargetMessageGenerator;
import com.ums.schedule.domain.target.TargetMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;

@Slf4j
@Component
public class EmailTargetUploadWorker {
    private final EmailTargetMessageGenerator generator;
    private final SendTargetUploadService uploadService;
    private final ThreadPoolTaskExecutor executor;

    @Autowired
    public EmailTargetUploadWorker(
            EmailTargetMessageGenerator generator,
            SendTargetUploadService uploadService,
            @Qualifier("targetUploadExecutor") ThreadPoolTaskExecutor executor) {
        this.generator = generator;
        this.uploadService = uploadService;
        this.executor = executor;
    }

    public List<CompletableFuture<TargetUploadResultList>> process(EmailGeneratorContext context, List<SendTargetGroupedList> groupList) {
        List<CompletableFuture<TargetUploadResultList>> futures = groupList.stream()
                .map(group -> CompletableFuture.supplyAsync(() -> {
                    printLog();
                    List<TargetUploadRowResult> targetRows = List.copyOf(group.targetRows());
                    List<TargetMessage> results = targetRows.stream()
                            .map(row -> {
                                SendTargetResult result = SendTargetResult.of(group, row);
                                TargetMessage targetMessage = generator.generate(context, result);
                                return targetMessage;
                            })
                            .toList();
                    return uploadService.upload(results);
                }, executor)).toList();

        CompletableFuture.allOf(
                        futures.stream()
                                .toArray(CompletableFuture[]::new))
                .join();

        return futures;
    }

    private void printLog() {
        ThreadPoolExecutor threadPool = executor.getThreadPoolExecutor();
        Thread thread = Thread.currentThread();
        if(threadPool != null) {
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
            return;
        }
        log.info("targetUploadExecutor is null");
    }
}
