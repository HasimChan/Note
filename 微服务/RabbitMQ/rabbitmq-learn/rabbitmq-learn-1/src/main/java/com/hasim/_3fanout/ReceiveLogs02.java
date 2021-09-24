package com.hasim._3fanout;

import com.hasim.utils.RabbitMqUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.net.URL;

/**
 * @Description 接收消息存储到磁盘
 * @Author Hasim
 * @Date 2021/9/17 14:55
 * @Version 1.0
 */
public class ReceiveLogs02 {
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
        System.out.println("等待接收消息,把接收到的消息写到文件.....");
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            File file = new File("./info/info.txt");
            FileUtils.writeStringToFile(file, message + "\n", "UTF-8", true);
            System.out.println("数据写入文件成功");
        };

        channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {
        });
    }
}
