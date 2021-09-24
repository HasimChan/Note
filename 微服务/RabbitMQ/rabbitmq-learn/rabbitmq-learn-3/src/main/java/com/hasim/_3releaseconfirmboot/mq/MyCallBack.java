package com.hasim._3releaseconfirmboot.mq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

/**
 * @Description 回调接口
 * @Author Hasim
 * @Date 2021/9/22 14:33
 * @Version 1.0
 */
@Component
@Slf4j
public class MyCallBack implements RabbitTemplate.ConfirmCallback {
    /**
     * @param correlationData 消息相关数据
     * @param ack             交换机是否收到消息
     * @param cause
     * @Description 交换机不管是否收到消息的回调方法
     */
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        String id = correlationData != null ? correlationData.getId() : "";
        if (ack) {
            log.info("交换机已经收到 id为:{}的消息", id);
        } else {
            log.info("交换机还未收到 id为:{}消息,由于原因:{}", id, cause);
        }
    }
}
