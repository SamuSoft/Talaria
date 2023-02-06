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
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
public class MessageEndpoints {

    List<Message> messages = new ArrayList<>();

    private Timestamp parseDate(String date) {
        var nDate = date;
        if (date.contains("+"))
            nDate = date.substring(0, date.indexOf('+'));
        return Timestamp.valueOf(nDate.replace('T', ' '));
    }

    @Resource(name = "queueService")
    private QueueService queueService = new QueueService();
    @Resource(name = "messageService")
    private MessageService messageService = new MessageService();

    @GetMapping("/messages")
    public List<Message> getAllMessagesSentBetween(String user, String from, String until) {
        //Doing a lot of string magic because Timestamp cannot be parsed again from its string format
        Timestamp fromT = null;
        Timestamp untilT = null;

        // If from time isn't set, only retrieve 1 day back
        if (from == null)
            fromT = Timestamp.from(Instant.now().minus(1, ChronoUnit.DAYS));
        else
            fromT = parseDate(from);

        // If until time isn't set, set to now
        if (until == null)
            untilT = Timestamp.from(Instant.now());
        else
            untilT = parseDate(until);


        return messageService.getMessagesByDate(user, fromT, untilT);
    }

    @GetMapping("/messages/unread")
    public List<Message> getUnreadMessages(String user) {
        return messageService.getUnreadMessages(user);
    }

    @GetMapping("/messages/{uuid}")
    public ResponseEntity<Message> getMessage(@PathVariable UUID uuid) {
        var message = messageService.getMessageById(uuid);
        if (message.isPresent())
            return new ResponseEntity<>(message.get(), HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/messages/{uuid}")
    public ResponseEntity<Message> deleteMessage(@PathVariable UUID uuid) {
        messageService.deleteMessage(uuid);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
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
