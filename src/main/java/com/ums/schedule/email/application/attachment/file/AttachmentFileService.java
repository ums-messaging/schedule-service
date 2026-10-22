package com.ums.schedule.email.application.attachment.file;

import com.ums.schedule.common.code.email.AttachmentType;
import com.ums.schedule.common.code.email.EmailCode;
import com.ums.schedule.common.code.mapper.EnumMapperFactory;
import com.ums.schedule.common.code.mapper.EnumMapperValue;
import com.ums.schedule.email.application.attachment.model.AttachmentFindResult;
import com.ums.schedule.orchestrator.adaptor.out.storage.AwsS3FileMetadataResponse;
import com.ums.schedule.orchestrator.adaptor.out.storage.AwsS3Repository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AttachmentFileService {
    private final EnumMapperFactory factory;
    private final AwsS3Repository fileRepository;

    public AttachmentFindResult getAttachmentResult(String type, String fileKey) {
        EnumMapperValue typeValue = factory.findEnumMapperValue(EmailCode.ATTACHMENT_TYPE, type);
        if(AttachmentType.DIRECT.code().equals(typeValue.code())) {
            AwsS3FileMetadataResponse response = fileRepository.getFileMetadata(fileKey);
            return AttachmentFindResult.of(typeValue, response);
        }
        return AttachmentFindResult.of(typeValue, fileKey); // 예외처리하기
    }
}
