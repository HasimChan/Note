package com.hasim._1receiveconfirm;

import com.hasim.utils.RabbitMqUtils;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

/**
 * @Description
 * @Author Hasim
 * @Date 2021/9/16 16:00
 * @Version 1.0
 */
public class Consumer1 {
    public static final String QUEUE_NAME = "hello";

    public static void main(String[] args) throws Exception {
        // 创建连接工厂
        Channel channel = RabbitMqUtils.getChannel();

        System.out.println("等待接收消息....");
        // 推送的消息如何进行消费的接口回调
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody());
            System.out.println(message);
            /**
             * 自动应答 -> 手动应答
             * 1. 消息标记tag (可以看成消息指针或位置，第几条消息)
             * 2. false 代表只应答接收到的那个消息 true 为应答所有接收消息包括接收到的
             */
            channel.basicAck(delivery.getEnvelope().getDeliveryTag(), true);
        };

        //取消消费的一个回调接口 如在消费的时候队列被删除掉了
        CancelCallback cancelCallback = (consumerTag) -> {
            System.out.println("消息消费被中断");
        };

        /**
         * 消费者消费消息
         * 1.消费哪个队列
         * 2.消费成功之后是否要自动应答 true代表自动应答 false手动应答
         * 3.消费者未成功消费的回调
         */
        boolean autoAck = false;
        channel.basicConsume(QUEUE_NAME, autoAck, deliverCallback, cancelCallback);
    }
}
