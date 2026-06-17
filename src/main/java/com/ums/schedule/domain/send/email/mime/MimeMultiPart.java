package com.ums.schedule.domain.send.email.mime;

public record MimeMultiPart(String contentType, String fileKey, String filename, String attachmentName, String downloadName) {
}
