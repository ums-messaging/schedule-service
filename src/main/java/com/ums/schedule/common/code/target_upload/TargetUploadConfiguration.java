package com.ums.schedule.common.code.target_upload;

import com.ums.schedule.common.code.mapper.EnumMapperType;

public enum TargetUploadConfiguration implements EnumMapperType {
    UPLOAD_KEY("prefix.key.upload", "대상자 업로드 경로"),
    DOWNLOAD_KEY("prefix.key.download", "대상자 업로드 결과 다운로드 경로"),
    FILE_BATCH_SIZE("file.batch.size", "파일 배치 크기"),
    PARTITION_SIZE("db.partition.size", "저장할 파티션 크기"),
    API_LIMIT_SIZE("api.limit.size", "API 대상자 업로드 크기"),
    FILE_LIMIT_SIZE("file.limit.size", "업로드 파일 제한 크기"),

    ;

    String value;
    String description;

    TargetUploadConfiguration(String value, String description) {
        this.value = value;
        this.description = description;
    }

    @Override
    public String code() {
        return this.name();
    }

    @Override
    public String value() {
        return this.value;
    }

    @Override
    public String description() {
        return this.description;
    }
}
