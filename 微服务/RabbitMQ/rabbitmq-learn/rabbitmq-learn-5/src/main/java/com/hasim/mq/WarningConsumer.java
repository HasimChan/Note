package com.hasim.mq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @Description 报警消费者
 * @Author Hasim
 * @Date 2021/9/22 17:01
 * @Version 1.0
 */
@Component
@Slf4j
public class WarningConsumer {
    public static final String WARNING_QUEUE_NAME = "warning.queue";
    public static final String BACKUP_QUEUE_NAME = "backup.queue";
    public static final String CONFIRM_QUEUE_NAME = "confirm.queue";

    @RabbitListener(queues = WARNING_QUEUE_NAME)
    public void receiveWarningMsg(Message message) {
        String msg = new String(message.getBody());
        log.error("报警发现不可路由消息：{}", msg);
    }

    @RabbitListener(queues = BACKUP_QUEUE_NAME)
    public void receiveBackupMsg(Message message) {
        String msg = new String(message.getBody());
        log.error("备份发现不可路由消息：{}", msg);
    }

    @RabbitListener(queues = CONFIRM_QUEUE_NAME)
    public void receiveConfirmMsg(Message message) {
        String msg = new String(message.getBody());
        log.error("接收确认消息：{}", msg);
    }
}
