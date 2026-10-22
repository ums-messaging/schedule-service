package com.ums.schedule.orchestrator.adaptor.out.queue;


import com.ums.schedule.email.application.send.job.model.SendJob;
import com.ums.schedule.email.domain.event.SendTargetResultList;
import com.ums.schedule.email.domain.target.group.DomainGroup;


import java.util.concurrent.CompletableFuture;

public interface SendTargetRequestDispatcher {
    CompletableFuture<SendTargetResultList> publish(SendJob job, DomainGroup domainGroup, SendTargetResultList resultList);
}
