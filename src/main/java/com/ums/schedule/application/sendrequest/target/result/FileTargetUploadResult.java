package com.ums.schedule.application.sendrequest.target.result;

import com.ums.schedule.adapter.storage.PresigendUrlResponse;
import com.ums.schedule.common.code.mapper.EnumMapperValue;
import com.ums.schedule.domain.sendrequest.target.upload.code.TargetUploadFormatEnum;

import java.time.LocalDateTime;

public record FileTargetUploadResult(
        String objectKey,
        String uploadUrl,
        LocalDateTime expiredAt
) {
    public static FileTargetUploadResult of(PresigendUrlResponse response) {
        return new FileTargetUploadResult(
                response.objectKey(),
                response.presignedUrl(),
                response.expiredAt()
        );
    }
}
