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
    public long sent;
    public boolean received;
    public String receiverMedium;
    public String senderMedium;
    public String message;

    public Message() {}
    public Message(UUID uuid,
                   long sent,
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

    public Message(com.talaria.portal.dto.Message message) {
        this(UUID.randomUUID(),
                Instant.now().getEpochSecond(),
                false,
                message.receiverMedium(),
                message.senderMedium(),
                message.message());
    }


}
