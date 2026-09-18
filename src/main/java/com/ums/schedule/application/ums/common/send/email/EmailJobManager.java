package com.ums.schedule.application.ums.common.send.email;

import com.github.f4b6a3.uuid.UuidCreator;
import com.ums.schedule.application.ums.common.request.model.SendRequestRequestedEvent;
import com.ums.schedule.application.ums.common.send.JobManager;
import com.ums.schedule.application.ums.common.send.model.EmailTargetGroupQueryResult;
import com.ums.schedule.common.code.common.ChannelType;
import com.ums.schedule.common.code.mapper.EnumMapperValue;
import com.ums.schedule.common.util.JsonUtil;
import com.ums.schedule.domain.request.SendRequest;
import com.ums.schedule.domain.request.SendRequestRepository;
import com.ums.schedule.domain.send.email.job.DomainGroup;
import com.ums.schedule.adapter.persistence.TargetMessageQueryRepository;
import com.ums.schedule.domain.send.email.job.EmailSendJob;
import com.ums.schedule.domain.send.email.job.SendJob;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Component
@Slf4j
@RequiredArgsConstructor
public class EmailJobManager implements JobManager {
    private final TargetMessageQueryRepository repository;
    private final KafkaTemplate<String, Object> template;

    @Override
    public boolean supports(EnumMapperValue mapperValue) {
        return ChannelType.EMAIL == ChannelType.valueOf(mapperValue.code());
    }

    @Transactional
    public void manage(SendJob sendJob) {
        List<EmailTargetGroupQueryResult> resultList =
                repository.findTargetGrouping(sendJob.uploadId());

        log.info("query.count = {}", resultList.size());

        EmailSendJob emailJob = EmailSendJob.of(sendJob, "email-send-request");
        resultList.stream()
                .forEach(result -> {
                    DomainGroup group = DomainGroup.of(emailJob, result);
                    template.send("email-send-request",
                            String.valueOf(group.groupId()),
                            JsonUtil.toJson(group));
                });
    }
}
