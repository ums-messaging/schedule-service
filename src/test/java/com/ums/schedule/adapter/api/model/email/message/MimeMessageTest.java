package com.ums.schedule.adapter.api.model.email.message;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;

import java.io.IOException;

class MimeMessageTest {
    @Test
    void makeMessageTest() {
        String html = "<html> " +
                "    <body>\n" +
                "        <img src=\"a.png\">\n" +
                "        <img src=\"b.png\">\n" +
                "        <img src=\"c.jpg\">\n" +
                "    </body>\n";
        Document document = Jsoup.parse(html);
        MessageDecorator message = new AttachmentMessage(new InlineMessage(new HtmlMessage(document)));
        try {
            StringBuffer buffer = new StringBuffer();
            message.decorate(buffer, MimeMessage.of(html));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    interface MessageDecorator {
        String decorate(StringBuffer buffer, MimeMessage mimeMessage) throws IOException;
    }

    abstract static class AbstractMessage implements MessageDecorator {
        private MessageDecorator message;

        public AbstractMessage(MessageDecorator message) {
            this.message = message;
        }

        @Override
        public String decorate(StringBuffer buffer, MimeMessage mimeMessage) throws IOException {
            String decorate = this.message.decorate(buffer, mimeMessage);
            return decorate;
        }
    }

    static class HtmlMessage implements MessageDecorator {
        private final String html;

        public HtmlMessage(Document document) {
            this.html = document.html();
        }

        @Override
        public String decorate(StringBuffer buffer, MimeMessage mimeMessage) throws IOException {
            buffer.append("Content-Type: \"multipart/alternative;\"; boundary=alt-boundary");
            buffer.append("\r\n");
            buffer.append("-- alt-boundary");
            buffer.append("Content-Type: text/html; charset=UTF-8");
            buffer.append("Content-Transfer-Encoding: 8bit");
            buffer.append("\r\n");

            buffer.append(mimeMessage.content());

            buffer.append("\r\n");
            buffer.append("--alt-boundary--");

            return buffer.toString();
        }
    }


    static class InlineMessage extends AbstractMessage {
//        private final MultiPartImage[] images;

        public InlineMessage(MessageDecorator message) {
            super(message);
        }

        @Override
        public String decorate(StringBuffer buffer, MimeMessage mimeMessage) throws IOException {
            buffer.append("Content-Type: multipart/related; boundary=\"--related-boundary\"");
            buffer.append("\r\n");
            buffer.append("--related-boundary");

            buffer.append(super.decorate(buffer, mimeMessage));
            buffer.append("\r\n");

            for (MultiPartImage image : mimeMessage.images()) {
                buffer.append("-- related-boundary");
                buffer.append("Content-Type: " + image.contentType());
                buffer.append("Content-Transfer-Encoding: base64");
                buffer.append("Content-ID: <" + image.contentId +">");
                buffer.append("Content-Disposition: inline; filename=\""+image.filepath()+"\"");
                buffer.append("\r\n");
                buffer.append(image.content());
            }
            buffer.append("--related-boundary--");
            buffer.append("\r\n");

            return buffer.toString();
        }
    }

    static class AttachmentMessage extends AbstractMessage {
        public AttachmentMessage(MessageDecorator message) {
            super(message);
        }

        @Override
        public String decorate(StringBuffer buffer, MimeMessage mimeMessage) throws IOException {
            buffer.append("Content-Type: multipart/mixed; boundary=\"mixed-boundary\"");
            buffer.append("\r\n");
            buffer.append("-- mixed-boundary");

            super.decorate(buffer, mimeMessage);

            for(MultiPart multiPart : mimeMessage.multiParts) {
                buffer.append("\r\n");
                buffer.append("-- mixed-boundary");
                buffer.append("Content-Type: "+ multiPart.contentType);
                buffer.append("Content-Transfer-Encoding: base64");
                buffer.append("Content-Disposition: attachment; filename=\""+multiPart.filename+"\"");
                buffer.append("\r\n");

//                InputStream fileContent = repository.getFileContent(mimeMessage.content());

                buffer.append(multiPart.filekey);
                buffer.append("\r\n");
            }
            buffer.append("-- mixed-boundary--");
            System.out.println(buffer.toString());
            return buffer.toString();
        }
    }

    record MimeMessage(String content, MultiPart[] multiParts, MultiPartImage[] images) {
        public static MimeMessage of(String html) {
            MultiPart[] multiParts = new MultiPart[3];

            multiParts[0] = MultiPart.of("test1.pdf");
            multiParts[1] = MultiPart.of("test1.html");
            multiParts[2] = MultiPart.of("test1.xlsx");

            Document doc = Jsoup.parse(html);
            Elements elements = doc.select("img");
            MultiPartImage[] images = new MultiPartImage[elements.size()];

            int i = 0;
            for (Element element : elements) {
                String src = element.attr("src");
                String cid = "cid:"+src;
                element.attr(src, cid);
                images[i] = new MultiPartImage("image/png", cid, src, "image content "+ i);
                i++;
            }
            return new MimeMessage(doc.html(), multiParts, images);
        }
    }

    record MultiPart(String contentType, String filekey, String filename) {
        public static MultiPart of(String filename) {
            String ext = filename.split("\\.")[1];
            return new MultiPart("application/"+ext, "test", filename);
        }
    }

    record MultiPartImage(String contentType, String contentId, String filepath, String content) {
    }
}