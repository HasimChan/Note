package com.hasim._2work;

import com.hasim.utils.RabbitMqUtils;
import com.rabbitmq.client.Channel;

/**
 * @Description 生产者
 * @Author Hasim
 * @Date 2021/9/15 16:56
 * @Version 1.0
 */
public class Producer {
    private static final String QUEUE_NAME = "hello";

    public static void main(String[] args) throws Exception {
        try (Channel channel = RabbitMqUtils.getChannel()) {
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);

            for (int i = 0; i < 50; i++) {
                String message = "hello world" + i;
                channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
                System.out.println("发送消息完成:" + message);

                Thread.sleep(i * 2);
            }
        }
    }
}
