package com.ums.schedule.domain.send.email.mime;


import com.ums.schedule.domain.send.email.code.EmailSendCommand;
import com.ums.schedule.domain.send.email.job.DomainGroupTarget;
import com.ums.schedule.domain.send.email.job.EmailSendJob;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public record MimeMessage(
        String mailFrom,
        String mailFromName,
        String receiver,
        String receiverName,
        String body,
        String imageDir,
        MultipartImage[] images,
        MimeMultiPart[] attachments) {
    public static MimeMessage of(EmailSendJob job, DomainGroupTarget target) {
        Document document = Jsoup.parse(target.content());
        MultipartImage[] images = MimeMessage.createImage(job.imageDir(), document);
        MimeMultiPart[] attachments = null;
//                MimeMessage.createAttachments(target.attachments());
        return new MimeMessage(job.mailFrom(), job.mailFromName(), target.receiverName(), target.receiverEmail(), document.html(), job.imageDir(), images, attachments);
    }

    private static MultipartImage[] createImage(String imageDir, Document document) {
        Elements elements = document.select("img");
        MultipartImage[] images = new MultipartImage[elements.size()];
        int i = 0;
        for(Element element : elements) {
            images[i] = MultipartImage.of(imageDir, element);
            document.attr("src", "cid:"+images[i].contentId());
        }
        return images;
    }

    public String getMailFrom() {
        return EmailSendCommand.MAIL_FROM.toCommand(this.mailFrom());
    }

    public String getReceiver() {
        return EmailSendCommand.RCPT_TO.toCommand(this.mailFrom());
    }
}
