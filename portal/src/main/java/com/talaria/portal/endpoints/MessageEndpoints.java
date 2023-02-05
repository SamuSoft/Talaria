package com.talaria.portal.endpoints;

import com.talaria.portal.model.Message;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
                        (from != null && message.sent().after(from)) ^
                                (until != null && message.sent().before(until)))
                .toList();
    }

    @GetMapping("/messages/{uuid}")
    public ResponseEntity<Message> getMessage(@PathVariable UUID uuid) {
        var maybeMessage = messages.stream()
                .filter(message -> message.uuid().equals(uuid))
                .findFirst();
        if (maybeMessage.isPresent())
            return new ResponseEntity<>(maybeMessage.get(), HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/messages/{uuid}")
    public ResponseEntity<Message> deleteMessage(@PathVariable UUID uuid) {
        var maybeMessage = messages.stream()
                .filter(message -> message.uuid().equals(uuid))
                .findFirst();
        if (maybeMessage.isPresent()) {
            messages.remove(maybeMessage.get());
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


    @PostMapping("/messages")
    public ResponseEntity<?> PostMessage(@RequestBody com.talaria.portal.externalModel.Message message) {
        var newMessage = new Message(message);
        messages.add(newMessage);
        return new ResponseEntity<>(newMessage, HttpStatus.ACCEPTED);
    }

}
