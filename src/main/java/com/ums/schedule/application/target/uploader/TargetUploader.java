package com.ums.schedule.application.target.uploader;

import com.ums.schedule.application.target.reader.model.TargetRowResult;
import com.ums.schedule.common.code.mapper.EnumMapperSelector;
import com.ums.schedule.domain.target.upload.TargetUploadReport;

import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

public interface TargetUploader extends EnumMapperSelector {
   Consumer<List<TargetRowResult>> upload(TargetUploadReport report, UUID messageId);
}
