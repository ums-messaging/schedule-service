package com.ums.schedule.application.message.email.result;

import java.io.File;

public record TemplateConversionResult(
        File tempFile,
        String objectKey,
        String templateContent
) {
    public static TemplateConversionResult of(File tempFile, String objectKey, String templateContent) {
        return new TemplateConversionResult(
                tempFile,
                objectKey,
                templateContent
        );
    }
}
