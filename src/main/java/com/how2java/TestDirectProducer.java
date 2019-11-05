package com.how2java;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class TestDirectProducer {

    public final static String QUEUE_NAME = "direct_queue";

    public static void main(String[] args) throws IOException, TimeoutException {

        RabbitMQUtil.checkServer();

        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");
        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();

        for (int i = 0; i < 100; i++) {
            String message = "direct消息" + i;
            channel.basicPublish("", QUEUE_NAME, null, message.getBytes("UTF-8"));
        }
        channel.close();
        connection.close();
    }

}
