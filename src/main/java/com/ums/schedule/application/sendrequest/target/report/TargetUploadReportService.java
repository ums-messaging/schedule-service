package com.ums.schedule.application.sendrequest.target.report;

import com.ums.schedule.adapter.api.request.SendRequestCreateRequest;
import com.ums.schedule.adapter.api.target.request.SendTargetCreateRequest;
import com.ums.schedule.adapter.api.target.request.FileTargetUploadRequest;
import com.ums.schedule.adapter.api.target.request.JsonTargetUploadRequest;
import com.ums.schedule.application.sendrequest.target.command.TargetUploadCreateCommand;
import com.ums.schedule.application.sendrequest.target.processor.TargetUploadProcessor;
import com.ums.schedule.application.sendrequest.target.result.TargetUploadResult;
import com.ums.schedule.common.code.mapper.EnumMapperFactory;
import com.ums.schedule.common.code.mapper.EnumMapperValue;
import com.ums.schedule.config.properties.TargetUploadProperties;
import com.ums.schedule.domain.sendrequest.message.ChannelMessage;
import com.ums.schedule.domain.sendrequest.code.ChannelTypeEnum;
import com.ums.schedule.domain.sendrequest.code.SendRequestEnumMapper;
import com.ums.schedule.domain.sendrequest.SendRequest;
import com.ums.schedule.domain.sendrequest.target.upload.TargetUploadReportJpaRepository;
import com.ums.schedule.domain.sendrequest.target.upload.code.TargetUploadReportEnumMapper;
import com.ums.schedule.domain.sendrequest.target.upload.code.TargetUploadTypeEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;


@Service
@RequiredArgsConstructor
public class TargetUploadReportService {
    private final EnumMapperFactory mapperFactory;
    private final TargetUploadProperties properties;
    private final TargetUploadReportJpaRepository repository;
    private final Map<TargetUploadTypeEnum, TargetUploadProcessor> processorMap;

    @Transactional
    public TargetUploadResult create(SendRequestCreateRequest request, SendRequest sendRequest, ChannelMessage message) {
        TargetUploadCreateCommand command = toCommand(sendRequest.getChannelType(), null, null);
//        TargetUploadReport targetUpload = TargetUploadReport.of(sendRequest, command, null);
//        TargetUploadProcessor processor = processorMap.get(targetUpload.getUploadType());
//        TargetUploadResult uploadResult = processor.requestUpload(targetUpload, request, message.getId().toString());

//        repository.save(targetUpload);

        return null;
    }

    private TargetUploadCreateCommand toCommand(ChannelTypeEnum channelType, FileTargetUploadRequest fileRequest, JsonTargetUploadRequest jsonRequest) {
        Map<TargetUploadReportEnumMapper, EnumMapperValue> toMapperMap = toMapperMap(fileRequest.uploadFormat(), fileRequest.uploadFormat());
        return toUploadCommand(channelType, toMapperMap, properties, jsonRequest.targetList());
    }
    private TargetUploadCreateCommand toUploadCommand(ChannelTypeEnum channelType, Map<TargetUploadReportEnumMapper, EnumMapperValue> toMapperMap, TargetUploadProperties properties, List<SendTargetCreateRequest> targetRequestList) {
        return TargetUploadCreateCommand.of(channelType, toMapperMap, properties, targetRequestList);
    }

    private Map<TargetUploadReportEnumMapper, EnumMapperValue> toMapperMap(String uploadType, String uploadFormat) {
        EnumMapperValue uploadTypeValue =
                mapperFactory.findEnumMapperValue(SendRequestEnumMapper.TARGET_UPLOAD_TYPE, uploadType);
        EnumMapperValue uploadFormatValue =
                mapperFactory.findEnumMapperValue(SendRequestEnumMapper.TARGET_UPLOAD_FORMAT, uploadFormat);
        return Map.of(TargetUploadReportEnumMapper.UPLOAD_TYPE, uploadTypeValue,
                TargetUploadReportEnumMapper.UPLOAD_FORMAT, uploadFormatValue);
    }
}
