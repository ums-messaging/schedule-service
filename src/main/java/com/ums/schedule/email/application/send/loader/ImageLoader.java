package com.ums.schedule.email.application.send.loader;

import com.ums.schedule.email.application.send.mime.model.MultiPartImage;
import lombok.RequiredArgsConstructor;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ImageLoader {
    public List<MultiPartImage> load(Document document) {
        return document.select("img")
                .stream()
                .map(v -> {
                    String src = v.attr("src");
                    MultiPartImage image = MultiPartImage.of(src);
                    v.attr("src", "cid:" + image.contentId());
                    return image;
                }).toList();
    }
}
