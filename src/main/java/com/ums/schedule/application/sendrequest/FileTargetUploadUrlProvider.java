package com.ums.schedule.application.sendrequest;

import com.ums.schedule.adapter.storage.AwsS3Repository;
import com.ums.schedule.adapter.storage.PresigendUrlResponse;
import com.ums.schedule.application.exception.TargetUploadFormatNotSupportedException;
import com.ums.schedule.application.sendrequest.target.result.FileTargetUploadResult;
import com.ums.schedule.common.code.mapper.EnumMapperFactory;
import com.ums.schedule.common.code.mapper.EnumMapperValue;
import com.ums.schedule.common.exception.EnumMapperException;
import com.ums.schedule.domain.sendrequest.target.upload.TargetUploadReport;
import com.ums.schedule.domain.sendrequest.target.upload.code.TargetUploadFormatEnum;
import com.ums.schedule.domain.sendrequest.target.upload.code.TargetUploadReportEnumMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FileTargetUploadUrlProvider implements TargetUploadHandler {
    private final EnumMapperFactory factory;
    private final AwsS3Repository fileRepository;

    public FileTargetUploadResult provide(TargetUploadReport targetUploadReport, String uploadFormat, String fileKeyPrefix) {
        PresigendUrlResponse response = fileRepository.generateUploadUrl(targetUploadReport.getUploadKey());
        EnumMapperValue uploadFormatValue = resolveUploadFormat(uploadFormat);
        targetUploadReport.initializeFileUploadAndGenerateUploadKey(uploadFormatValue, fileKeyPrefix);
        return FileTargetUploadResult.of(response);
    }


    private EnumMapperValue resolveUploadFormat(String uploadFormat) {
        try {
            return Optional.ofNullable(uploadFormat)
                    .map(format -> factory.findEnumMapperValue(TargetUploadReportEnumMapper.UPLOAD_FORMAT, format))
                    .orElse(EnumMapperValue.fromEnumMapperType(TargetUploadFormatEnum.CSV));
        } catch (EnumMapperException e) {
            throw TargetUploadFormatNotSupportedException.of(e);
        }
    }

}
