package com.talaria.portal.clients;

import com.talaria.portal.entities.Message;
import com.talaria.portal.repositories.MessageRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@Service("messageService")
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public Message getMessageById(UUID id) {
        return messageRepository.getReferenceById(id);
    }

    public List<Message> getMessagesByDate(String receiver, Timestamp from, Timestamp until) {
        var query = entityManager.createQuery(
                "SELECT m FROM Message m WHERE m.receiverMedium = :receiver AND " +
                        "(m.sent >= :from OR m.sent <= :until)", Message.class);

        return query.setParameter("receiver", receiver)
                .setParameter("from", from)
                .setParameter("until", until)
                .getResultList();
    }

    public List<Message> getUnreadMessages(String receiver) {
        // This whole thing should really just be one single DB Transaction
        var query = entityManager.createQuery(
                "SELECT m FROM Message m WHERE m.receiverMedium = :receiver AND m.received = false",
                Message.class);

        var Messages = query.setParameter("receiver", receiver)
                .getResultList();

        for (var m : Messages) {
            // Looping over all messages, but as said above, should be something else
            entityManager.createQuery("UPDATE Message m SET m.received = true WHERE m.id = :id")
                    .setParameter("id", m.uuid);
        }
        return Messages;
    }

    public void deleteMessage(UUID id){
        messageRepository.deleteById(id);
    }

    public void addMessage(Message message) {
        messageRepository.save(message);
    }
}
