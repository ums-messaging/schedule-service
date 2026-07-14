package com.ums.schedule.application.sendrequest.target.processor;

import com.ums.schedule.adapter.storage.AwsS3Repository;
import com.ums.schedule.common.code.mapper.EnumMapperValue;
import com.ums.schedule.common.code.target_upload.TargetUploadTypeEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.ums.schedule.common.code.target_upload.TargetUploadTypeEnum.FILE;


@Service
@RequiredArgsConstructor
public class FileTargetUploadProcessor implements TargetUploadProcessor {
    private final AwsS3Repository repository;

    @Override
    public boolean supports(EnumMapperValue mapperValue) {
        return FILE == TargetUploadTypeEnum.valueOf(mapperValue.code());
    }

//    @Override
//    public TargetUploadResult requestUpload(TargetUploadReport targetUpload, SendRequestCreateRequest request, String messageId) {
//        PresigendUrlResponse response = repository.generateUploadUrl(targetUpload.getUploadKey());
//        return TargetUploadResult.of(targetUpload, response);
//    }
}
