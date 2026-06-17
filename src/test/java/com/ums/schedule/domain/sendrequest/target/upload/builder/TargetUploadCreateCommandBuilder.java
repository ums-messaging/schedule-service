package com.ums.schedule.domain.sendrequest.target.upload.builder;

import com.ums.schedule.application.sendrequest.target.data.TargetMessageData;
import com.ums.schedule.application.sendrequest.target.command.TargetUploadCreateCommand;
import com.ums.schedule.common.code.mapper.EnumMapperValue;
import com.ums.schedule.domain.sendrequest.code.ChannelTypeEnum;
import com.ums.schedule.domain.sendrequest.target.upload.code.TargetUploadFormatEnum;
import com.ums.schedule.domain.sendrequest.target.upload.code.TargetUploadTypeEnum;

import java.util.List;

public class TargetUploadCreateCommandBuilder {
    private ChannelTypeEnum channelType;
    private EnumMapperValue uploadType = EnumMapperValue.fromEnumMapperType(TargetUploadTypeEnum.FILE);
    private EnumMapperValue uploadFormat = EnumMapperValue.fromEnumMapperType(TargetUploadFormatEnum.CSV);
    private String filePrefix = "target/upload";
    private Integer maxSize = 1000;
    private List<TargetMessageData> targetList = List.of();

    public static TargetUploadCreateCommandBuilder builder() {
        return new TargetUploadCreateCommandBuilder();
    }

    public TargetUploadCreateCommandBuilder uploadType(EnumMapperValue uploadType) {
        this.uploadType = uploadType;
        return this;
    }

    public TargetUploadCreateCommandBuilder uploadFormat(EnumMapperValue uploadFormat) {
        this.uploadFormat = uploadFormat;
        return this;
    }

    public TargetUploadCreateCommandBuilder filePrefix(String filePrefix) {
        this.filePrefix = filePrefix;
        return this;
    }

    public TargetUploadCreateCommandBuilder maxSize(Integer maxSize) {
        this.maxSize = maxSize;
        return this;
    }

    public TargetUploadCreateCommandBuilder targetList(List<TargetMessageData> targetList) {
        this.targetList = targetList;
        return this;
    }

    public TargetUploadCreateCommand build() {
        return new TargetUploadCreateCommand(channelType, uploadType, uploadFormat, filePrefix,maxSize, targetList);
    }
}
