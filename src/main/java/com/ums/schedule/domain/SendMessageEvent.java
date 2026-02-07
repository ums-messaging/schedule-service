package com.ums.schedule.domain;

import com.ums.schedule.common.code.ErrorCodeEnum;
import com.ums.schedule.common.code.SendMessageStatusEnum;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
public class SendMessageEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private SendMessageStatusEnum status;

    @Column(name = "error_code", nullable = false)
    @Enumerated(EnumType.STRING)
    private ErrorCodeEnum errorCode;

    @Column(name = "error_msg")
    private String error_msg;

    @Column(name = "attempt_order_no", nullable = false)
    private Integer attemptOrderNo;

    @Column(name = "evented_at", nullable = false)
    private LocalDateTime eventedAt;

    @Getter
    @ManyToOne
    @JoinColumn(name = "send_message_id", nullable = false)
    private SendMessage sendMessage;

    public void applySendMessage(SendMessage sendMessage) {
        this.sendMessage = sendMessage;
        this.sendMessage.getSendMessageEvents().add(this);
    }
}
