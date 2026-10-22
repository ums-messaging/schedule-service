package com.ums.schedule.orchestrator.adaptor.out.storage;

import com.ums.schedule.common.code.email.FileStorageType;

import java.io.InputStream;

public interface FileRepository {
    boolean supports(FileStorageType storageType);
    InputStream getFileContent(String key);
}
