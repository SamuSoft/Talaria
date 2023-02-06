package com.talaria.portal.clients;

import com.talaria.portal.entities.Message;
import com.talaria.portal.repositories.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service("messageService")
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    public Message getMessageById(UUID id){
        return messageRepository.getReferenceById(id);
    }

    public void addMessage(Message message) {
        messageRepository.save(message);
    }
}
