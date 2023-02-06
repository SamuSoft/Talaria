package com.talaria.portal.dto;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;

public record Message(UUID uuid,
                      Timestamp sent,
                      boolean received,
                      String receiverMedium,
                      String senderMedium,
                      String message) {
    public Message(com.talaria.portal.entities.Message message){
        this(message.uuid, Timestamp.from(Instant.ofEpochMilli(message.sent)),
        message.received,
        message.receiverMedium,
        message.senderMedium,
        message.message);
    }
}
