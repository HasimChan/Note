package com.hasim._2work;

import com.hasim.utils.RabbitMqUtils;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

/**
 * @Description 消费者
 * @Author Hasim
 * @Date 2021/9/15 16:50
 * @Version 1.0
 */
public class Consumer1 {
    public static final String QUEUE_NAME = "hello";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtils.getChannel();

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String receivedMessage = new String(delivery.getBody());
            System.out.println("接收到消息：" + receivedMessage);
            // 手动应答
            channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
        };

        CancelCallback cancelCallback = (consumerTag) -> {
            System.out.println(consumerTag + "消费者取消消费接口回调逻辑");
        };

        System.out.println("c1消费者启动等待消费");

        // 非公平分发  在手动ack的情况下才生效，自动ack不生效
        // 注：1. 写在消费者端 2. 写在外面
        int prefetchCount = 1;
        channel.basicQos(prefetchCount); // 预取值

        boolean autoAck = false; // false: 手动ack
        channel.basicConsume(QUEUE_NAME, autoAck, deliverCallback, cancelCallback);
    }
}
