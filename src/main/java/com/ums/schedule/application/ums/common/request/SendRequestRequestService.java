package com.ums.schedule.application.ums.common.request;

import com.ums.schedule.application.ums.common.send.JobManager;
import com.ums.schedule.common.code.common.CommonCode;
import com.ums.schedule.common.code.mapper.EnumMapperFactory;
import com.ums.schedule.common.code.mapper.EnumMapperValue;
import com.ums.schedule.domain.request.SendRequest;
import com.ums.schedule.domain.request.SendRequestRepository;
import com.ums.schedule.domain.schedule.Schedule;
import com.ums.schedule.domain.send.email.job.SendJob;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
public class SendRequestRequestService {
    private final SendRequestRepository repository;
    private final EnumMapperFactory factory;
    private final List<JobManager> jobManagers;

    @Transactional
    public Long request(Long requestId) {
        SendRequest sendRequest = repository.findById(requestId).orElseThrow();
        Schedule schedule = sendRequest.getSchedule();
        sendRequest.requestSend();

        if(schedule.isRealtime()) {
            EnumMapperValue channelTypeValue = factory.findEnumMapperValue(CommonCode.CHANNEL_TYPE, sendRequest.getChannelType().value());
            JobManager jobManager = jobManagers.stream()
                    .filter(manager -> manager.supports(channelTypeValue))
                    .findFirst()
                    .orElseThrow();
            SendJob job = sendRequest.createJob();
            jobManager.manage(job);
            sendRequest.sendStart();
        }

        return sendRequest.getId();
    }
}
