package com.ums.schedule.orchestrator.domain.upload;

import com.ums.schedule.common.code.common.FileContentType;
import com.ums.schedule.common.code.mapper.EnumMapperValue;

public record FileUploadMetadata(
        FileContentType contentType,
        Long fileSize
) {
    public static FileUploadMetadata of(EnumMapperValue contentType, Long contentLength, Long fileLimitSize) {
        FileUploadMetadata metadata = new FileUploadMetadata(
                FileContentType.valueOf(contentType.code()), contentLength);
        metadata.validateContentType();
        metadata.validateFileSize(fileLimitSize);
        return metadata;
    }

    private void validateContentType() {
        if(!availableContentType(contentType)) {
            throw new IllegalArgumentException("업로드 파일은 xlsx, xls, csv 타입이어야 합니다. ");
        }
    }
    private void validateFileSize(Long fileLimitSize) {
        if(fileSize == 0L) {
            throw new IllegalArgumentException("파일 내용이 존재하지 않습니다.");
        } else if(fileSize > fileLimitSize) {
            throw new IllegalArgumentException("파일 크기가 초과 되었습니다.");
        }
    }
    private boolean availableContentType(FileContentType contentType) {
        return contentType == FileContentType.CSV || contentType == FileContentType.EXCEL_XLSX || contentType == FileContentType.EXCEL_XLX;
    }
}
