package com.ums.schedule.email.application.send.mime.model;
import com.github.f4b6a3.uuid.UuidCreator;
import com.ums.schedule.email.application.send.mime.writer.MimeWriter;

import java.io.IOException;

public record MultiPartImage(
        String contentId,
        String src
) {
    public static MultiPartImage of(String contentId, String src) {
        return new MultiPartImage(contentId, src);
    }

    public static MultiPartImage of(String src) {
        String contentId = UuidCreator.getTimeOrdered().toString();
        return new MultiPartImage(contentId, src);
    }

    public void setHeader(MimeWriter writer, String contentType, String fileKey) throws IOException {
        writer.write("-- related-boundary");
        writer.write("Content-Type: " + contentType);
        writer.write("Content-Transfer-Encoding: base64");
        writer.write("Content-ID: <" + contentType +">");
        writer.write("Content-Disposition: inline; filename=\""+fileKey+"\"");
    }
}
