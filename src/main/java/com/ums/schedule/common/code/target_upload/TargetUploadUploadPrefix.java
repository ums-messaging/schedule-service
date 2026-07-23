package com.ums.schedule.common.code.target_upload;

import com.ums.schedule.common.code.mapper.EnumMapperType;

public enum TargetUploadUploadPrefix implements EnumMapperType {
    UPLOAD_KEY("upload.key.prefix", "대상자 업로드 경로"),
    DOWNLOAD_KEY("download.key.prefix", "대상자 업로드 결과 다운로드 경로")
    ;

    String value;
    String description;

    TargetUploadUploadPrefix(String value, String description) {
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
