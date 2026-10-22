package com.ums.schedule.orchestrator.application.requests;

import com.ums.schedule.orchestrator.adaptor.out.persistance.jpa.repository.SendRequestQueryRepository;
import com.ums.schedule.orchestrator.adaptor.out.storage.AwsS3Repository;
import com.ums.schedule.orchestrator.adaptor.out.storage.PresigendUrlResponse;
import com.ums.schedule.orchestrator.application.requests.result.SendRequestQueryResult;
import com.ums.schedule.orchestrator.application.requests.result.SendRequestRequestContext;
import com.ums.schedule.orchestrator.domain.upload.SendRequestUpload;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SendRequestFileService {
    private final AwsS3Repository fileRepository;
    private final SendRequestQueryRepository repository;

    public SendRequestRequestContext findSendRequestById(Long id) {
        SendRequestQueryResult result = repository.findSendRequestForRequest(id).orElseThrow();
        SendRequestUpload upload = result.sendRequestUpload();
        PresigendUrlResponse response = fileRepository.generateDownloadUrl(upload.objectKey());
        return SendRequestRequestContext.of(result.sendRequest(), result.schedule(), response);
    }

}
