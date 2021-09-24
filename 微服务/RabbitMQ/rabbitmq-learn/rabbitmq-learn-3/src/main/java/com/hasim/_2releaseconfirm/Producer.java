package com.hasim._2releaseconfirm;

import com.hasim.utils.RabbitMqUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmCallback;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * @Description
 * @Author Hasim
 * @Date 2021/9/17 9:43
 * @Version 1.0
 */
public class Producer {
    private static final String QUEUE_NAME = "hello";
    public static final int MESSAGE_COUNT = 1000;

    public static void main(String[] args) throws Exception {
        publishMessageIndividually();
        publishMessageBatch();
        publishMessageAsync();
    }

    /**
     * 单个发布确认
     */
    public static void publishMessageIndividually() throws Exception {
        Channel channel = RabbitMqUtils.getChannel();
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);

        // 开启发布确认
        channel.confirmSelect();
        long begin = System.currentTimeMillis();
        for (int i = 0; i < MESSAGE_COUNT; i++) {
            String message = i + "";
            channel.basicPublish("", QUEUE_NAME, null, message.getBytes(StandardCharsets.UTF_8));
            // 服务端返回false或者超时时间内未返回，生产者可以消息重发
            boolean flag = channel.waitForConfirms();
            if (flag) {
//                System.out.println("消息发送成功");
            }
        }
        long end = System.currentTimeMillis();
        System.out.println("发布" + MESSAGE_COUNT + "个单独确认消息,耗时" + (end - begin) + "ms");
    }

    /**
     * 批量发布确认，就是单个确认的升级版
     */
    public static void publishMessageBatch() throws Exception {
        Channel channel = RabbitMqUtils.getChannel();
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);

        // 开启发布确认
        channel.confirmSelect();
        // 批量确认消息大小
        int batchSize = 100;
        // 未确认消息个数
        int outstandingMessageCount = 0;
        long begin = System.currentTimeMillis();
        for (int i = 0; i < MESSAGE_COUNT; i++) {
            String message = i + "";
            channel.basicPublish("", QUEUE_NAME, null, message.getBytes(StandardCharsets.UTF_8));
            outstandingMessageCount++;
            if (outstandingMessageCount == batchSize) {
                channel.waitForConfirms();
                outstandingMessageCount = 0;
            }
        }
        //为了确保还有剩余没有确认消息 再次确认 if (outstandingMessageCount > 0) { channel.waitForConfirms();

        long end = System.currentTimeMillis();
        System.out.println("发布" + MESSAGE_COUNT + "个批量确认消息,耗时" + (end - begin) + "ms");
    }

    /**
     * 异步发布确认，前面两种都是同步，发就完事了
     * 原理：用一个线程安全且有序的表存储消息(作为已发送消息的备份)
     * 若发送成功则从该列表中删除对应消息
     */
    public static void publishMessageAsync() throws Exception {
        Channel channel = RabbitMqUtils.getChannel();
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);

        // 开启发布确认
        channel.confirmSelect();

        /**
         * 线程安全 有序 的一个哈希表，适用于高并发的情况
         * 1.轻松的将 序号 与 消息 进行关联
         * 2.轻松批量删除条目 只要给到序列号
         * 3.支持并发访问
         * 相当于已发送消息的备份
         */
        ConcurrentSkipListMap<Long, String> outstandingConfirms = new ConcurrentSkipListMap<>(); // JUC 的

        /**
         * 消息确认成功回调
         * 1.消息序列号
         * 2.true  可以确认小于等于当前序列号的消息
         *   false 确认当前序列号消息
         */
        ConfirmCallback ackCallback = (sequenceNumber, multiple) -> {
            // 清除确认成功的，剩下的就是确认失败的
            if (multiple) { // 批量
                // 返回小于等于当前序列号的消息
                ConcurrentNavigableMap<Long, String> confirmed = outstandingConfirms.headMap(sequenceNumber, true);
                // 清除该部分未确认消息
                confirmed.clear();
            } else { // 非批量
                // 只清除该部分未确认消息
                outstandingConfirms.remove(sequenceNumber);
            }
        };

        /**
         * 消息确认失败回调
         * 1.消息序列号(标识)
         * 2.true  可以确认小于等于当前序列号的消息
         *   false 确认当前序列号消息
         */
        ConfirmCallback nackCallback = (sequenceNumber, multiple) -> {
            String message = outstandingConfirms.get(sequenceNumber); // 获取确认失败的消息
            System.out.println("发布的消息" + message + "未被确认，序列号" + sequenceNumber);
        };

        /**
         * 添加异步确认的消息监听器，监听哪些消息成功了，哪些消息失败了
         * 1.确认收到消息的回调
         * 2.未收到消息的回调
         */
        channel.addConfirmListener(ackCallback, nackCallback);

        long begin = System.currentTimeMillis();
        for (int i = 0; i < MESSAGE_COUNT; i++) {
            String message = "消息" + i;
            /**
             * channel.getNextPublishSeqNo()获取下一个消息的序列号
             * 通过序列号与消息体进行一个关联
             * 全部都是未确认的消息体
             */
            outstandingConfirms.put(channel.getNextPublishSeqNo(), message);
            channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
        }
        long end = System.currentTimeMillis();
        System.out.println("发布" + MESSAGE_COUNT + "个异步确认消息,耗时" + (end - begin) + "ms");
    }
}
