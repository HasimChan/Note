package com.hasim._4routing;

import com.hasim.utils.RabbitMqUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description 发送日志，存在问题：发送的东西反了，错误日志发送到绑定了info和warning的队列
 * @Author Hasim
 * @Date 2021/9/17 15:41
 * @Version 1.0
 */
public class EmitLogDirect {
    private static final String EXCHANGE_NAME = "direct_logs";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtils.getChannel();
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
        //创建多个bindingKey
        HashMap<String, String> bindingKeyMap = new HashMap<>();
        bindingKeyMap.put("info", "普通info信息");
        bindingKeyMap.put("warning", "警告warning信息");
        bindingKeyMap.put("error", "错误error信息");
        //debug没有消费这接收这个消息 所有就丢失了
        bindingKeyMap.put("debug", "调试debug信息");
        for (Map.Entry<String, String> bindingKeyEntry : bindingKeyMap.entrySet()) {
            String key = bindingKeyEntry.getKey();
            String message = bindingKeyEntry.getValue();
            channel.basicPublish(EXCHANGE_NAME, key, null, message.getBytes(StandardCharsets.UTF_8));
            System.out.println("生产者发出消息: " + key + ":" + message);
        }
    }
}
