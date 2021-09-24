package com.hasim._3fanout;

import com.hasim.utils.RabbitMqUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

/**
 * @Description 接收消息打印到控制台
 * @Author Hasim
 * @Date 2021/9/17 14:55
 * @Version 1.0
 */
public class ReceiveLogs01 {
    public static final String EXCHANGE_NAME = "logs";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtils.getChannel();
        channel.exchangeDeclare(EXCHANGE_NAME, "fanout"); // 定义交换机
        /**
         * 生成临时队列 队列名随机
         * 消费者断开与该队列连接时自动删除
         */
        String queueName = channel.queueDeclare().getQueue();
        //把该临时队列绑定我们的exchange 其中routingkey(也称之为binding key)为空字符串
        channel.queueBind(queueName, EXCHANGE_NAME, "");
        System.out.println("等待接收消息,把接收到的消息打印在屏幕.....");
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println("控制台打印接收到的消息" + message);
        };

        channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {
        });
    }
}
