package com.hasim.utils;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * @Description RabbitMQ 工具类
 * @Author Hasim
 * @Date 2021/9/15 16:47
 * @Version 1.0
 */
public class RabbitMqUtils {
    /**
     * 获取一个连接的channel
     */
    public static Channel getChannel() throws Exception {
        // 创建连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("124.71.114.210");
        factory.setUsername("admin");
        factory.setPassword("admin");

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        return channel;
    }
}
