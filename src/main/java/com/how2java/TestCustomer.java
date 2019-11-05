package com.how2java;

import cn.hutool.core.util.RandomUtil;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class TestCustomer {

    public final static String EXCHANGE_NAME = "fanout_exchange";

    public static void main(String[] args) throws IOException, TimeoutException {

        RabbitMQUtil.checkServer();

        final String customerName = "consumer-" + RandomUtil.randomString(5);

        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");
        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();
        channel.exchangeDeclare(EXCHANGE_NAME, "fanout");//交换机，交换机模式
        String queueName = channel.queueDeclare().getQueue();//产生队列，并获取到队列名
        channel.queueBind(queueName, EXCHANGE_NAME, "");//队列与交换机绑定，参数为（队列名，交换机名，路由键）

        System.out.println(customerName + "等待接收消息。");

        Consumer consumer = new DefaultConsumer(channel) {

            @Override
            public void handleDelivery(String s, Envelope envelope, AMQP.BasicProperties basicProperties, byte[] bytes) throws IOException {
                String message = new String(bytes, "Utf-8");
                System.out.println(customerName+"接收到消息:'" + message + "'");
            }
        };

        channel.basicConsume(queueName, true, consumer);


    }
}
