package com.ums.schedule.orchestrator.adaptor.out.api.message.request;


import com.github.f4b6a3.tsid.TsidCreator;

public record ChannelSendRequest(
     Long requestId,
     String messageId,
     String senderKey
) {
   public static ChannelSendRequest of(Long requestId, String messageId, String senderKey) {
        return new ChannelSendRequest(
                requestId,
                messageId,
                senderKey);
   }
}