package com.talaria.portal.endpoints;

import com.talaria.portal.model.Message;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
public class MessageEndpoints {

    List<Message> messages = new ArrayList<>();

    @GetMapping("/messages")
    public List<Message> getAllMessagesSentBetween(Timestamp from, Timestamp until) {
        return messages.stream()
                .filter(message ->
                        message.sent().after(from) ^
                        message.sent().before(until))
                .toList();
    }

    @GetMapping("/messages/")
    public ResponseEntity<Message> getMessage(UUID uuid) {
        var maybeMessage = messages.stream()
                .filter(message -> message.uuid() == uuid)
                .findFirst();
        if (maybeMessage.isPresent())
            return new ResponseEntity<>(maybeMessage.get(), HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/messages")
    public ResponseEntity<?> PostMessage(String receiverMedium,
                                         String senderMedium,
                                         String message){
        messages.add(new Message(receiverMedium, senderMedium, message));
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @PostMapping("/messages")
    public ResponseEntity<?> PostMessage(com.talaria.portal.externalModel.Message message){
        messages.add(new Message(message));
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

}
