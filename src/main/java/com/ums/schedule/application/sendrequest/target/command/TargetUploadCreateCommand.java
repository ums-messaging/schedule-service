package com.ums.schedule.application.sendrequest.target.command;

import com.ums.schedule.adapter.api.target.request.SendTargetCreateRequest;
import com.ums.schedule.application.sendrequest.target.data.TargetMessageData;
import com.ums.schedule.common.code.mapper.EnumMapperValue;
import com.ums.schedule.config.properties.TargetUploadProperties;
import com.ums.schedule.domain.sendrequest.code.ChannelTypeEnum;
import com.ums.schedule.domain.sendrequest.target.upload.code.TargetUploadReportEnumMapper;

import java.util.List;
import java.util.Map;

public record TargetUploadCreateCommand(
        ChannelTypeEnum channelType,
        EnumMapperValue uploadType,
        EnumMapperValue uploadFormat,
        String filePrefix,
        Integer targetListMaxSize,
        List<TargetMessageData> targetList
) {
    public static TargetUploadCreateCommand of(ChannelTypeEnum channelType, Map<TargetUploadReportEnumMapper, EnumMapperValue> mapperValueMap, TargetUploadProperties properties, List<SendTargetCreateRequest> targetList) {
        return new TargetUploadCreateCommand(
                channelType,
                mapperValueMap.get(TargetUploadReportEnumMapper.UPLOAD_TYPE),
                mapperValueMap.get(TargetUploadReportEnumMapper.UPLOAD_FORMAT),
                properties.getUploadKey(),
                properties.getUploadMaxSize(),
                targetList.stream()
                        .map(target -> target.toTargetData())
                        .toList()
        );
    }
}
