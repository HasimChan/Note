package com.hasim._1deadletter.ttl;

import com.hasim.utils.RabbitMqUtils;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;

import java.nio.charset.StandardCharsets;

/**
 * @Description 死信条件：TTL过期、队列已满、消息被拒且不重新入队
 * @Author Hasim
 * @Date 2021/9/18 9:56
 * @Version 1.0
 */
public class Producer {
    private static final String NORMAL_EXCHANGE = "normal_exchange";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtils.getChannel();
        channel.exchangeDeclare(NORMAL_EXCHANGE, BuiltinExchangeType.DIRECT);
        // 设置消息的TTL时间
        AMQP.BasicProperties properties = new AMQP.BasicProperties().builder().expiration("10000").build(); // 10s
        for (int i = 1; i < 11; i++) {
            String message = "info" + i;
            // 针对每条消息指定TTL
            channel.basicPublish(NORMAL_EXCHANGE, "normal", properties, message.getBytes(StandardCharsets.UTF_8));
            System.out.println("生产者发送消息:" + message);
        }
    }
}
