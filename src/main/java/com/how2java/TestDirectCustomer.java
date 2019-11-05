package com.how2java;

import cn.hutool.core.util.RandomUtil;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class TestDirectCustomer {

    public final static String QUEUE_NAME = "direct_queue";

    public static void main(String[] args) throws IOException, TimeoutException {

        RabbitMQUtil.checkServer();

        final String customerName = "customer-" + RandomUtil.randomString(5);

        ConnectionFactory connectionFactory = new ConnectionFactory();
        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();

        channel.queueDeclare(QUEUE_NAME, false, false, true, null);

        System.out.println(customerName + "等待接收消息");

        Consumer consumer = new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String s, Envelope envelope, AMQP.BasicProperties basicProperties, byte[] bytes) throws IOException {
                String message = new String(bytes, "UTF-8");
                System.out.println(customerName + "接收到消息：" + message + "。");
            }
        };

        channel.basicConsume(QUEUE_NAME, consumer);

    }
}
