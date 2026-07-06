package com.ums.schedule.adapter.api.model.email.smtp;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.util.StringUtils;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

class SmtpClientTest {
    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {
        String html = "<html> " +
                "    <body>\n" +
                "        <img src=\"a.png\">\n" +
                "        <img src=\"b.png\">\n" +
                "        <img src=\"c.jpg\">\n" +
                "    </body>\n";

        CompletableFuture<String> future1 = extracted(html);
        CompletableFuture<String> future2 = extracted(html);
        CompletableFuture<String> future3 = extracted(html);
        CompletableFuture<String> future4 = extracted(html);
        CompletableFuture<String> future5 = extracted(html);
        CompletableFuture<String> future6 = extracted(html);

        CompletableFuture<Void> future = CompletableFuture.allOf(future1, future2, future3, future4, future5,future6);
        future.get();

//        future1.get();
    }

    private static void log(String result) {
        System.out.println("["+Thread.currentThread().getName() + "]" + result);
    }

    private static CompletableFuture<String> extracted(String html) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                Socket socket = new Socket("123.0.0.1", 8989);
                socket.setSoTimeout(3);
                EmailClient client = new EmailClient(socket, MimeMessage.of(html));
                return client.send();
            } catch (IOException e) {
                System.out.println("IOEXCEPTION");
                e.printStackTrace();
            }
            return "";
        });
    }

    static class EmailClient {
        private final SmtpServerHandler handler;
        private final Socket socket;
        private final MimeMessage message;

       public EmailClient(Socket socket, MimeMessage message) {
           this.socket = socket;
           this.handler = new SmtpServerConnectorHandler();
           this.message = message;
       }

        public String send() {
            InputStream is = null;
            OutputStream os = null;
            BufferedReader reader = null;
            BufferedWriter writer = null;

            try {
                is = socket.getInputStream();
                os = socket.getOutputStream();

                reader = new BufferedReader(new InputStreamReader(is));
                writer = new BufferedWriter(new OutputStreamWriter(os));
                handler.sendCommand(os, reader, message);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if(socket !=null) socket.close();
                    if(is != null) is.close();
                    if(os != null) os.close();
                    if(reader!= null) reader.close();
                    if(writer != null) writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    return "fail";
                }
            }
            return "success";
        }
    }

    interface SmtpServerHandler {
        String sendCommand(OutputStream os, BufferedReader reader, MimeMessage message) throws IOException;

        default List<String> send(OutputStream os, BufferedReader reader,  String command) throws IOException {
            List<String> responses = new ArrayList<>();
            os.write(command.getBytes(StandardCharsets.UTF_8));
            os.write("\r\n".getBytes(StandardCharsets.UTF_8));
            os.flush();
            while(true) {
                String line = reader.readLine();
                System.out.println("["+Thread.currentThread().getName() + "] >> " + line);
                if(line.length() == 0) {
                    break;
                }
                responses.add(line);
            }
            return responses;
        }

        default String getError(List<String> responses, String successCode) {
            return responses.stream()
                    .filter(res -> !res.startsWith(successCode))
                    .findFirst()
                    .orElseGet(() -> "");
        }
    }

    static class SmtpServerConnectorHandler implements SmtpServerHandler {
        private String elho = "localhost";
        private SmtpServerHandler handler = new SmtpServerMailFromHandler();

        @Override
        public String sendCommand(OutputStream os, BufferedReader reader, MimeMessage message) throws IOException {
            String response = reader.readLine();

            if(response.startsWith("220")) {
                List<String> responses = send(os, reader, "ELHO " + elho);
                String error = getError(responses, "250");

                if(StringUtils.hasText(error)) {
                    send(os, reader, "RSET");
                    return error;
                }
                return handler.sendCommand(os, reader, message);
            }
            return "500 error";
        }
    }


    static class SmtpServerMailFromHandler implements SmtpServerHandler {
        private SmtpServerHandler handler = new SmtpServerDataHandler();

        @Override
        public String sendCommand(OutputStream os, BufferedReader reader, MimeMessage message) throws IOException {
            String from = "MAIL FROM:<"+message.mailFrom()+">";
            String rcpt = "RCPT TO:<"+message.receiver()+">";

            List<String> fromResponses = send(os, reader, from);
            List<String> toResponses = send(os, reader, rcpt);

            String fromError = getError(fromResponses, "250");
            String toError = getError(toResponses, "250");

            if(StringUtils.hasText(fromError) || StringUtils.hasText(toError)) {
                send(os, reader, "RSET");
                return StringUtils.hasText(fromError) ? fromError : toError;
            }
            return handler.sendCommand(os, reader,message);
        }
    }

    static class SmtpServerDataHandler implements SmtpServerHandler {
        private SmtpServerHandler handler = new SmtpServerQuitHandler();
        private MessageDecorator writer;

        @Override
        public String sendCommand(OutputStream os, BufferedReader reader, MimeMessage message) throws IOException {
            Document document = Jsoup.parse(message.content);
            List<String> data = send(os, reader, "DATA");
            if(data.get(0).startsWith("354")) {
                this.writer = new AttachmentMessage(new InlineMessage(new HtmlMessage(document)));
                writer.decorate(os, message);
                List<String> result = send(os, reader, ".");
                if(result.get(0).startsWith("250")) {
                    return handler.sendCommand(os, reader, message);
                }
            }
            os.write("RSET".getBytes(StandardCharsets.UTF_8));
            os.flush();
            return reader.readLine();
        }
    }

    static class SmtpServerQuitHandler implements SmtpServerHandler {

        @Override
        public String sendCommand(OutputStream os, BufferedReader reader, MimeMessage message) throws IOException {
            List<String> responses = send(os, reader, "QUIT");
            String error = getError(responses, "221");
            return StringUtils.hasText(error) ? error : responses.get(responses.size()-1);
        }
    }

    interface MessageDecorator {
        String decorate(OutputStream os, MimeMessage mimeMessage) throws IOException;
    }

    abstract static class AbstractMessage implements MessageDecorator {
        private MessageDecorator message;

        public AbstractMessage(MessageDecorator message) {
            this.message = message;
        }

        @Override
        public String decorate(OutputStream os, MimeMessage mimeMessage) throws IOException {
            String decorate = this.message.decorate(os, mimeMessage);
            return decorate;
        }
    }

    static class HtmlMessage implements MessageDecorator {
        private final String html;

        public HtmlMessage(Document document) {
            this.html = document.html();
        }

        @Override
        public String decorate(OutputStream os, MimeMessage mimeMessage) throws IOException {
            StringBuilder builder = new StringBuilder();
            builder.append("Content-Type: \"multipart/alternative;\"; boundary=alt-boundary");
            builder.append("\r\n");
            builder.append("-- alt-boundary");
            builder.append("\r\n");
            builder.append("Content-Type: text/html; charset=UTF-8");
            builder.append("\r\n");
            builder.append("Content-Transfer-Encoding: 8bit");
            builder.append("\r\n");

            builder.append(mimeMessage.content());
            builder.append("\r\n");

            builder.append("\r\n");
            builder.append("--alt-boundary--");
            builder.append("\r\n");

            os.write(builder.toString().getBytes(StandardCharsets.UTF_8));

            return builder.toString();
        }
    }

    static class InlineMessage extends AbstractMessage {
//        private final MultiPartImage[] images;

        public InlineMessage(MessageDecorator message) {
            super(message);
        }

        @Override
        public String decorate(OutputStream os, MimeMessage mimeMessage) throws IOException {
            StringBuilder builder = new StringBuilder();
            builder.append("Content-Type: multipart/related; boundary=\"--related-boundary\"");
            builder.append("\r\n");
            builder.append("--related-boundary");
            builder.append("\r\n");

            os.write(builder.toString().getBytes(StandardCharsets.UTF_8));
            super.decorate(os, mimeMessage);
            builder.append("\r\n");

            builder = new StringBuilder();

            for (MultiPartImage image : mimeMessage.images()) {
                builder.append("-- related-boundary");
                builder.append("\r\n");
                builder.append("Content-Type: " + image.contentType());
                builder.append("\r\n");
                builder.append("Content-Transfer-Encoding: base64");
                builder.append("\r\n");
                builder.append("Content-ID: <" + image.contentId +">");
                builder.append("\r\n");
                builder.append("Content-Disposition: inline; filename=\""+image.filepath()+"\"");
                builder.append("\r\n");
                builder.append(image.content());
                builder.append("\r\n");
            }
            builder.append("--related-boundary--");
            builder.append("\r\n");

            os.write(builder.toString().getBytes(StandardCharsets.UTF_8));
            return builder.toString();
        }
    }

    static class AttachmentMessage extends AbstractMessage {
        public AttachmentMessage(MessageDecorator message) {
            super(message);
        }

        @Override
        public String decorate(OutputStream os, MimeMessage mimeMessage) throws IOException {
            StringBuilder builder = new StringBuilder();

            builder.append("Content-Type: multipart/mixed; boundary=\"mixed-boundary\"");
            builder.append("\r\n");
            builder.append("-- mixed-boundary");

            os.write(builder.toString().getBytes(StandardCharsets.UTF_8));

            super.decorate(os, mimeMessage);

            for(MultiPart multiPart : mimeMessage.multiParts()) {
                builder.append("\r\n");
                builder.append("-- mixed-boundary");
                builder.append("\r\n");
                builder.append("Content-Type: "+ multiPart.contentType);
                builder.append("\r\n");
                builder.append("Content-Transfer-Encoding: base64");
                builder.append("\r\n");
                builder.append("Content-Disposition: attachment; filename=\""+multiPart.filename+"\"");
                builder.append("\r\n");
//                InputStream fileContent = repository.getFileContent(mimeMessage.content());

                builder.append(multiPart.filekey);
                builder.append("\r\n");
            }
            builder.append("-- mixed-boundary--");
            builder.append("\r\n");
            os.write(builder.toString().getBytes(StandardCharsets.UTF_8));
            System.out.println(builder.toString());
            return builder.toString();
        }
    }

    record MimeMessage(String mailFrom, String receiver, String content, MultiPart[] multiParts, MultiPartImage[] images) {
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
            return new MimeMessage("test@test.com", "jang314@naver.com",doc.html(), multiParts, images);
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