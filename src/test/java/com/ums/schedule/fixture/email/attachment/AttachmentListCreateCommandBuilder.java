package com.ums.schedule.fixture.email.attachment;

import com.ums.schedule.application.ums.email.attachment.model.AttachmentContext;
import com.ums.schedule.application.ums.email.attachment.model.AttachmentListCreateCommand;
import com.ums.schedule.application.ums.email.convert.EmailConvertPolicy;
import com.ums.schedule.domain.message.email.EmailSendMessage;
import com.ums.schedule.fixture.email.convert.EmailConvertPolicyBuilder;

import java.util.List;

public class AttachmentListCreateCommandBuilder {
    private EmailSendMessage sendMessage;
    private EmailConvertPolicy convertPolicy;
    private List<AttachmentContext> attachments;

    public static AttachmentListCreateCommandBuilder builder() {
        return new AttachmentListCreateCommandBuilder();
    }

    private AttachmentListCreateCommandBuilder() {
        this.convertPolicy = givenEmailConvertPolicy();
        this.attachments = List.of();
    }

    private EmailConvertPolicy givenEmailConvertPolicy() {
        return EmailConvertPolicyBuilder.builder().build();
    }

    public AttachmentListCreateCommandBuilder convertedPolicy(EmailConvertPolicy convertPolicy) {
        this.convertPolicy = convertPolicy;
        return this;
    }

    public AttachmentListCreateCommandBuilder attachmentList(List<AttachmentContext> attachments) {
        this.attachments = attachments;
        return this;
    }

    public AttachmentListCreateCommandBuilder sendMessage(EmailSendMessage sendMessage) {
        this.sendMessage = sendMessage;
        return this;
    }


    public AttachmentListCreateCommand build() {
        return new AttachmentListCreateCommand(
                sendMessage,
                convertPolicy,
                attachments
        );
    }
}
