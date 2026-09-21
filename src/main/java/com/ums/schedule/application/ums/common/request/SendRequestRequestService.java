package com.ums.schedule.application.ums.common.request;

import com.ums.schedule.application.ums.common.send.JobManager;
import com.ums.schedule.common.code.common.CommonCode;
import com.ums.schedule.common.code.mapper.EnumMapperFactory;
import com.ums.schedule.common.code.mapper.EnumMapperValue;
import com.ums.schedule.domain.request.SendRequest;
import com.ums.schedule.domain.request.SendRequestRepository;
import com.ums.schedule.domain.schedule.Schedule;
import com.ums.schedule.domain.send.email.job.SendJob;
import com.ums.schedule.domain.target.upload.TargetUploadReport;
import com.ums.schedule.domain.target.upload.TargetUploadReportJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class SendRequestRequestService {
    private final TargetUploadReportJpaRepository reportRepository;
    private final SendRequestRepository repository;
    private final EnumMapperFactory factory;
    private final List<JobManager> jobManagers;

    @Transactional
    public Long request(Long requestId, String uploadId) {
        SendRequest sendRequest = repository.findById(requestId).orElseThrow();
        Schedule schedule = sendRequest.getSchedule();
        sendRequest.requestSend();

        if(schedule.isRealtime()) {
            EnumMapperValue channelTypeValue = factory.findEnumMapperValue(CommonCode.CHANNEL_TYPE, sendRequest.getChannelType().value());
            JobManager jobManager = jobManagers.stream()
                    .filter(manager -> manager.supports(channelTypeValue))
                    .findFirst()
                    .orElseThrow();
            TargetUploadReport uploadReport = Optional.ofNullable(uploadId)
                    .map(id -> UUID.fromString(id))
                    .map(id -> reportRepository.findById(id).orElseThrow())
                    .orElseGet(() -> sendRequest.getCurrentTargetUpload());
                    ;
            SendJob job = sendRequest.createJob(uploadReport);
            jobManager.manage(job);
            sendRequest.sendStart();
        }

        return sendRequest.getId();
    }

}
