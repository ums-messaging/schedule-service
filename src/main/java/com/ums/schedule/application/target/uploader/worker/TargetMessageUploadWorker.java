package com.ums.schedule.application.target.uploader.worker;

import com.ums.schedule.application.sendrequest.target.SendTargetUploadService;
import com.ums.schedule.application.sendrequest.target.result.TargetUploadResultList;
import com.ums.schedule.application.target.uploader.model.EmailGeneratorContext;
import com.ums.schedule.application.target.uploader.reader.model.TargetUploadRowResult;
import com.ums.schedule.application.ums.common.target.context.SendTargetGroupedList;
import com.ums.schedule.application.ums.common.target.result.SendTargetResult;
import com.ums.schedule.application.ums.common.template.loader.EmailTemplateLoader;
import com.ums.schedule.application.ums.common.template.loader.model.EmailTemplate;
import com.ums.schedule.application.ums.email.generator.EmailTargetMessageGenerator;
import com.ums.schedule.domain.message.email.EmailSendMessage;
import com.ums.schedule.domain.message.email.EmailSendMessageJpaRepository;
import com.ums.schedule.domain.request.SendRequest;
import com.ums.schedule.domain.request.SendRequestRepository;
import com.ums.schedule.domain.target.TargetMessage;
import com.ums.schedule.domain.target.message.EmailTargetMessage;
import com.ums.schedule.domain.target.upload.TargetUploadReport;
import com.ums.schedule.domain.target.upload.TargetUploadReportJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

@Slf4j
@Component
@RequiredArgsConstructor
public class TargetMessageUploadWorker {
    private final SendRequestRepository repository;
    private final EmailSendMessageJpaRepository messageRepository;
    private final EmailTemplateLoader loader;
    private final EmailTargetMessageGenerator generator;
    private final SendTargetUploadService uploadService;

    @Transactional
    public List<TargetUploadResultList> process(Long requestId, List<SendTargetGroupedList> groupList) {
        SendRequest sendRequest = repository.findById(requestId).orElseThrow();
        EmailSendMessage sendMessage = messageRepository.findById(sendRequest.getSendMessage().getId()).orElseThrow();
        EmailTemplate emailTemplate = loader.loadTemplate(sendMessage);

        TargetUploadReport currentTargetUpload = sendRequest.getCurrentTargetUpload();
        EmailGeneratorContext context = EmailGeneratorContext.of(currentTargetUpload, emailTemplate, sendMessage);

        List<List<CompletableFuture<TargetMessage>>> futures = generateTargetMessage(context, groupList);
        List<TargetUploadResultList> list = awaitTargetMessageTask(futures)
                .map(messages -> uploadService.upload(currentTargetUpload.getId(), messages))
                .toList();
        long startMs = System.currentTimeMillis();
        log.info("upload.time = {}", startMs-System.currentTimeMillis());
        return list;
    }

    private Stream<List<TargetMessage>> awaitTargetMessageTask(List<List<CompletableFuture<TargetMessage>>> futures) {
        return futures.stream()
                .map(future -> {
                    CompletableFuture.allOf(
                                    future.stream()
                                            .toArray(CompletableFuture[]::new))
                            .join();
                    return future.stream()
                            .map(CompletableFuture::join)
                            .toList();
                });
    }

    private List<List<CompletableFuture<TargetMessage>>> generateTargetMessage(EmailGeneratorContext context,  List<SendTargetGroupedList> groupList) {
        return groupList.stream()
                .map(group -> group.targetRows().stream()
                        .map(row -> generate(context, group, row))
                        .toList()
                ).toList();
    }

    private CompletableFuture<TargetMessage> generate(EmailGeneratorContext context, SendTargetGroupedList group, TargetUploadRowResult row) {
        return CompletableFuture.supplyAsync(() -> {
            SendTargetResult result = SendTargetResult.of(group, row);
            TargetMessage targetMessage = generator.generate(context, result);
            return targetMessage;
        });
    }

//    private void printLog() {
//        ThreadPoolExecutor threadPool = executor.getThreadPoolExecutor();
//        Thread thread = Thread.currentThread();
//        if(threadPool != null) {
//            log.info(
//                    "targetUploadExecutor - threadName= {}. poolSize={}, activeCount={}, corePoolSize={}, maxPoolSize={}, queueSize={}, queueRemainingCapacity={}, completedTaskCount={}, taskCount={}",
//                    thread.getName(),
//                    threadPool.getPoolSize(),
//                    threadPool.getActiveCount(),
//                    threadPool.getCorePoolSize(),
//                    threadPool.getMaximumPoolSize(),
//                    threadPool.getQueue().size(),
//                    threadPool.getQueue().remainingCapacity(),
//                    threadPool.getCompletedTaskCount(),
//                    threadPool.getTaskCount()
//            );
//            return;
//        }
//        log.info("targetUploadExecutor is null");
//    }
}
