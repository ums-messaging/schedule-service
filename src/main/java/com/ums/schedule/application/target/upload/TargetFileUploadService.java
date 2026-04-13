package com.ums.schedule.application.target.upload;

import com.ums.schedule.adapter.storage.AwsS3Repository;
import com.ums.schedule.adapter.storage.PresigendUrlResponse;
import com.ums.schedule.code.EnumMapperValue;
import com.ums.schedule.application.target.dto.SendTargetDto;
import com.ums.schedule.domain.target.upload.TargetUpload;
import com.ums.schedule.domain.request.SendRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.ums.schedule.domain.send.code.TargetUploadTypeEnum.*;

@Service
@RequiredArgsConstructor
public class TargetFileUploadService implements TargetUploadService {
    private final AwsS3Repository repository;

    public TargetUpload create(SendRequest request, List<SendTargetDto> dtos) {
        PresigendUrlResponse response = repository.generateUploadUrl(ContentTypeEnum.CSV, request.getCustomerRequestKey().getCustomerId());

        TargetUpload targetUpload = TargetUpload.of(FILE, request);
        targetUpload.applyObjectKey(response.objectKey());
        return targetUpload;
    }

    @Override
    public boolean supports(EnumMapperValue mapperValue) {
        return false;
    }
}
