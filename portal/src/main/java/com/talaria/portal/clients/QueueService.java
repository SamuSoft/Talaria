package com.talaria.portal.clients;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.talaria.portal.entities.Message;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

@Service("queueService")
public class QueueService {
    private final static String QUEUE_NAME = "Talaria";
    private ConnectionFactory factory;

    public QueueService() {
        factory = new ConnectionFactory();
        factory.setHost("localhost");
    }

    public void send(Message message) throws IOException {
        var mapper = new ObjectMapper().writer();
        String json = mapper.writeValueAsString(message);
        System.out.println(json);
        try (var connection = factory.newConnection();
             var channel = connection.createChannel()) {
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            channel.basicPublish("", QUEUE_NAME, null, json.getBytes());
        } catch (TimeoutException ex) {
        }
        System.out.println("Message published: " + message.uuid);
    }

}
