package com.ums.schedule.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(name = "uq_send_request_receiver", columnNames = {"send_request_id", "receiver_id"})
})
public class SendMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "send_message_id")
    @Getter
    private Long id;

    @Column(name = "receiver_id", nullable = false)
    private String receiverId;

    @Column(name = "template_param")
    private String templateParam;

    @Getter
    @ManyToOne
    @JoinColumn(name = "send_request_id", nullable = false)
    private SendRequest sendRequest;

    @Getter
    @OneToMany(mappedBy = "sendMessage", cascade = {CascadeType.PERSIST})
    private List<SendMessageEvent> sendMessageEvents = new ArrayList<>();

    public void applySendRequest(SendRequest sendRequest) {
        this.sendRequest = sendRequest;
        this.sendRequest.getSendMessages().add(this);
    }
}
