package com.ums.schedule.email.application.send.loader.model;


import com.ums.schedule.email.application.send.mime.model.MimeAttachFile;
import com.ums.schedule.email.application.send.mime.model.MultiPartImage;

import java.nio.file.Path;
import java.util.List;

public record TemplateLoadResult(
    Path bodyMessage,
    List<MultiPartImage> images,
    List<MimeAttachFile> files
) {
    public static TemplateLoadResult of(Path bodyMessage, List<MultiPartImage> images, List<MimeAttachFile> files) {
        return new TemplateLoadResult(
                bodyMessage,
                images,
                files
        );
    }
}
