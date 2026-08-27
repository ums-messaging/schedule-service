package com.ums.schedule.application.target;

import com.ums.schedule.application.target.report.model.TargetUploadRequestResult;
import com.ums.schedule.common.code.target_upload.TargetUploadStatus;
import com.ums.schedule.domain.request.SendRequest;
import com.ums.schedule.domain.target.upload.TargetUploadReport;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
@RequiredArgsConstructor
public class TargetUploadRequestService {

    @Transactional
    public TargetUploadRequestResult request(TargetUploadReport report) {
        TargetUploadStatus status = report.requestTargetUpload();
        SendRequest sendRequest = report.getSendRequest();
        sendRequest.changeStateByTargetUploadReport(status);
        return TargetUploadRequestResult.of(sendRequest, sendRequest.getSendMessage(),
                status);
    }
}

