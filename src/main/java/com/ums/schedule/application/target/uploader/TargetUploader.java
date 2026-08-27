package com.ums.schedule.application.target.uploader;

import com.ums.schedule.application.ums.common.target.context.SendTargetGroupedList;
import com.ums.schedule.common.code.mapper.EnumMapperSelector;

import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

public interface TargetUploader extends EnumMapperSelector {
   Consumer<List<SendTargetGroupedList>> upload(UUID reportId, UUID messageId);
}
