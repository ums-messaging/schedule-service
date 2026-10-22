package com.ums.schedule.email.application.send.manager;

import com.ums.schedule.common.code.email.EmailResultCode;
import com.ums.schedule.common.code.request.SendGroupEventType;
import com.ums.schedule.email.adaptor.out.queue.kafka.SendGroupEventKafkaDispatcher;
import com.ums.schedule.email.adaptor.out.smtp.SmtpSessionFactory;
import com.ums.schedule.email.adaptor.out.smtp.model.SmtpSessionInfo;
import com.ums.schedule.email.application.send.mime.helper.MimeWriteHelper;

import com.ums.schedule.email.application.send.mime.model.MimeMessage;

import com.ums.schedule.email.application.send.mime.writer.MimeWriter;
import com.ums.schedule.email.common.code.SendTargetErrorTypeCode;
import com.ums.schedule.email.domain.common.SendResult;
import com.ums.schedule.email.domain.event.SendGroupEventResult;
import com.ums.schedule.email.domain.event.SendTargetResultList;
import com.ums.schedule.email.domain.target.SendTargetError;
import com.ums.schedule.email.domain.target.SendTargetRequest;
import com.ums.schedule.email.domain.target.group.DomainGroup;
import com.ums.schedule.orchestrator.adaptor.out.storage.AwsS3Repository;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
@Component
public class SendManager {
    private final SendGroupEventKafkaDispatcher dispatcher;
    private final SmtpSessionFactory sessionFactory;
    private final MimeWriteHelper helper;
    private final AwsS3Repository agent;
    public SendManager(SmtpSessionFactory sessionFactory,
                       @Qualifier("mimeAttachmentWriter") MimeWriteHelper helper,
                       AwsS3Repository agent,
                       SendGroupEventKafkaDispatcher dispatcher
                       ) {
        this.sessionFactory = sessionFactory;
        this.helper = helper;
        this.agent = agent;
        this.dispatcher = dispatcher;
    }

    public SendTargetResultList send(Long sendRequestId, DomainGroup group, MimeMessage mimeMessage, Map<Integer, List<SendTargetRequest>> groupList) throws IOException {
        List<SendTargetRequest> successList = new ArrayList<>();
        List<SendTargetError> errorList = new ArrayList<>();
        Set<Integer> keySet = groupList.keySet();
        SendGroupEventResult result = SendGroupEventResult.of();
        for (Integer idx : keySet) {
            Path tempFile = null;
            SmtpSessionInfo session = sessionFactory.createSession(mimeMessage.domain());
            try {
                for (SendTargetRequest target : groupList.get(idx)) {
                    tempFile = Files.createTempFile(target.getId() + "-", ".eml");
                    try (BufferedWriter w = Files.newBufferedWriter(
                            tempFile,
                            StandardCharsets.UTF_8,
                            StandardOpenOption.TRUNCATE_EXISTING)) {
                        MimeWriter writer = MimeWriter.of(session, w, mimeMessage);
                        writer.setHeaders(target.getReceiver().getEmail(), target.getMessageVariable());
                        writer.setMailFrom();
                        writer.setReceiver(target.getReceiver().getEmail());
                        writer.setData();
                        helper.write(writer, target);
                        writer.setFinish();
                        send(tempFile, target);
                        successList.add(target);
                    } catch (IOException | TemplateException e) {
                        errorList.add(SendTargetError.of(target, SendTargetErrorTypeCode.TEMPLATE_ERROR, e.getMessage()));
                        throw new RuntimeException(e);
                    } catch (RuntimeException e) {
                        result = SendGroupEventResult.of(EmailResultCode.FAIL, e.getMessage());
                        errorList.add(SendTargetError.of(target, SendTargetErrorTypeCode.SMTP_ERROR, e.getMessage()));
                    }
                }
            } catch (IOException e){
                log.error("error = ", e);
                SendTargetResultList resultList = SendTargetResultList.of(sendRequestId, group, successList, errorList);
                dispatcher.publish(SendGroupEventType.SEND_COMPLETED, result, resultList);
                throw e;
            } finally {
                Files.deleteIfExists(tempFile);
            }
        }
        SendTargetResultList resultList = SendTargetResultList.of(sendRequestId, group, successList, errorList);
        dispatcher.publish(SendGroupEventType.SEND_COMPLETED, result, resultList);

        return resultList;
    }

    private void send(Path tempFile, SendTargetRequest target) {
        DomainGroup group = target.getGroup();
        agent.upload(tempFile.toFile(), "email/mime/"+ group.groupId() + "/" + target.getId() + ".eml");
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
