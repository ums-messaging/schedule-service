package com.ums.schedule.orchestrator.adaptor.out.storage;

import com.ums.schedule.common.code.email.FileStorageType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

@Component
@RequiredArgsConstructor
public class LocalStorageRepository implements FileRepository {
    @Override
    public boolean supports(FileStorageType storageType) {
        return FileStorageType.LOCAL == storageType;
    }

    public InputStream getFileContent(String key) {
        try(InputStream is = Files.newInputStream(Paths.get(key))) {
            return is;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
