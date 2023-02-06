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
    private Connection connection;
    private Channel channel;

    public QueueService() {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try {
            connection = factory.newConnection();
            channel = connection.createChannel();
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        } catch (IOException | TimeoutException ex) {
            System.err.println("Failed to connect to RabbitMQ");
        }
    }

    public void send(Message message) throws IOException {
        generalSend(message);
    }

    private void generalSend(Object object) throws IOException {
        var mapper = new ObjectMapper().writer();
        String json = mapper.writeValueAsString(object);
        channel.basicPublish("", QUEUE_NAME, null, json.getBytes());
    }
}
