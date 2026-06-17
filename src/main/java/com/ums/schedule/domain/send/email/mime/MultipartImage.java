package com.ums.schedule.domain.send.email.mime;


import org.jsoup.nodes.Element;

public record MultipartImage(
        String contentId,
        String src,
        String fileKey
) {

    public static MultipartImage of(String fileKey, String contentId) {
        String[] fileKeys = fileKey.split("/");
        return new MultipartImage(contentId, fileKey, fileKeys[fileKeys.length-1]);
    }

    public static MultipartImage of(String bucketName, Element element) {
        String src = element.attr("src");
        String[] filePaths = src.split("\\/");
        String[] filenames = filePaths[filePaths.length - 1].split("\\.");
        String filename = filenames[0];
        return new MultipartImage(filename, src , bucketName+src);
    }
}
