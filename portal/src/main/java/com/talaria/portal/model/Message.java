package com.talaria.portal.model;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;

public record Message(UUID uuid,
                      Timestamp sent,
                      boolean received,
                      String receiverMedium,
                      String senderMedium,
                      String message) {
    public Message(String receiverMedium, String senderMedium, String message){
        this(UUID.randomUUID(), Timestamp.from(Instant.now()), false, receiverMedium, senderMedium, message);
    }

    public Message(com.talaria.portal.externalModel.Message message){
        this(UUID.randomUUID(),
                Timestamp.from(Instant.now()),
                false,
                message.receiverMedium(),
                message.senderMedium(),
                message.message());
    }
}
