package com.ums.schedule.send.application.assembler;

import com.ums.schedule.attachment.infrastructure.AwsS3Repository;
import com.ums.schedule.attachment.infrastructure.PresigendUrlResponse;
import com.ums.schedule.common.code.EnumMapperValue;
import com.ums.schedule.message.application.command.EmailMessageCommand;
import com.ums.schedule.message.code.ContentTypeEnum;
import com.ums.schedule.send.EmailSendJob;
import com.ums.schedule.send.application.model.command.SendTargetCreateCommand;
import com.ums.schedule.send.application.model.dto.SendRequestDto;
import com.ums.schedule.send.application.model.dto.SendTargetDto;
import com.ums.schedule.send.application.model.dto.TargetUploadDto;
import com.ums.schedule.send.application.service.EmailSendRequestService;
import com.ums.schedule.send.code.TargetUploadTypeEnum;
import com.ums.schedule.send.domain.request.EmailBody;
import com.ums.schedule.send.domain.request.EmailSendRequest;
import com.ums.schedule.send.domain.request.SendRequest;
import com.ums.schedule.send.domain.request.upload.TargetUpload;
import com.ums.schedule.template.domain.email.EmailTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.ums.schedule.send.code.TargetUploadTypeEnum.*;

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
