package com.talaria.portal.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;


import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;

@Entity
public class Message {

    @Id
    public UUID uuid;
    public Timestamp sent;
    public boolean received;
    public String receiverMedium;
    public String senderMedium;
    public String message;

    public Message() {}
    public Message(UUID uuid,
                   Timestamp sent,
                   boolean received,
                   String receiverMedium,
                   String senderMedium,
                   String message) {
        this.uuid = uuid;
        this.sent = sent;
        this.received = received;
        this.receiverMedium = receiverMedium;
        this.senderMedium = senderMedium;
        this.message = message;
    }

    public Message(String receiverMedium, String senderMedium, String message) {
        this(UUID.randomUUID(), Timestamp.from(Instant.now()), false, receiverMedium, senderMedium, message);
    }

    public Message(com.talaria.portal.dto.Message message) {
        this(UUID.randomUUID(),
                Timestamp.from(Instant.now()),
                false,
                message.receiverMedium(),
                message.senderMedium(),
                message.message());
    }


}
