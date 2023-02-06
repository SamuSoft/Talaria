package com.talaria.portal.endpoints;

import com.talaria.portal.clients.MessageService;
import com.talaria.portal.clients.QueueService;
import com.talaria.portal.entities.Message;
import jakarta.annotation.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
public class MessageEndpoints {

    List<Message> messages = new ArrayList<>();

    @Resource(name = "queueService")
    private QueueService queueService = new QueueService();
    @Resource(name = "messageService")
    private MessageService messageService = new MessageService();

    @GetMapping("/messages")
    public List<Message> getAllMessagesSentBetween(Timestamp from, Timestamp until) {
        return messages.stream()
                .filter(message ->
                        (from != null && message.sent.after(from)) ^
                                (until != null && message.sent.before(until)))
                .toList();
    }

    @GetMapping("/messages/{uuid}")
    public ResponseEntity<Message> getMessage(@PathVariable UUID uuid) {
        var message = messageService.getMessageById(uuid);
        if (message != null)
            return new ResponseEntity<>(message, HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/messages/{uuid}")
    public ResponseEntity<Message> deleteMessage(@PathVariable UUID uuid) {
        var maybeMessage = messages.stream()
                .filter(message -> message.uuid.equals(uuid))
                .findFirst();
        if (maybeMessage.isPresent()) {
            messages.remove(maybeMessage.get());
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


    @PostMapping("/messages")
    public ResponseEntity<?> PostMessage(@RequestBody com.talaria.portal.dto.Message message) {
        var newMessage = new Message(message);
        try {
            queueService.send(newMessage);
        } catch (IOException ex) {
            return new ResponseEntity<>(HttpStatus.I_AM_A_TEAPOT);
        }
        messageService.addMessage(newMessage);
        return new ResponseEntity<>(newMessage, HttpStatus.ACCEPTED);
    }

}
