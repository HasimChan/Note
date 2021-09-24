package com.hasim.mq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.UUID;

/**
 * @Description 回退消息的消息生产者(集成了controller和回退消息相关组件)
 * @Author Hasim
 * @Date 2021/9/22 15:02
 * @Version 1.0
 */
@RestController
@Slf4j
@RequestMapping("backup")
public class MessageProducer implements RabbitTemplate.ConfirmCallback, RabbitTemplate.ReturnsCallback {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @PostConstruct
    private void init() {
        rabbitTemplate.setConfirmCallback(this);

        // true: 交换机无法将消息进行路由时，会将该消息返回给生产者
        // false: 如果发现消息无法进行路由，则直接丢弃
        rabbitTemplate.setMandatory(true);

        //设置回退消息交给谁处理
        rabbitTemplate.setReturnsCallback(this);
    }

    // 交换机收到消息的回调函数
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        String id = correlationData != null ? correlationData.getId() : "";
        if (ack) {
            log.info("交换机收到消息确认成功, id:{}", id);
        } else {
            log.error("消息 id:{}未成功投递到交换机,原因是:{}", id, cause);
        }
    }

    // 回退消息
    @Override
    public void returnedMessage(ReturnedMessage returnedMessage) {
        log.info("消息:{}被服务器退回，退回原因:{}, 交换机是:{}, 路由 key:{}",
                new String(returnedMessage.getMessage().getBody()),
                returnedMessage.getReplyText(),
                returnedMessage.getExchange(),
                returnedMessage.getRoutingKey());
    }

    @GetMapping("sendMessage")
    public void sendMessage(String message) {
        //让消息绑定一个id值
        CorrelationData correlationData1 = new CorrelationData(UUID.randomUUID().toString());
        rabbitTemplate.convertAndSend("confirm.exchange", "key1", message + "key1", correlationData1);
        log.info("发送消息 id为:{}内容为{}", correlationData1.getId(), message + "key1");

        CorrelationData correlationData2 = new CorrelationData(UUID.randomUUID().toString());
        rabbitTemplate.convertAndSend("confirm.exchange", "key2", message + "key2", correlationData2);
        log.info("发送消息 id为:{}内容为{}", correlationData2.getId(), message + "key2");
    }
}
