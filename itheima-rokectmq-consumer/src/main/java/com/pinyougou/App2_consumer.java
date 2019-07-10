package com.pinyougou;


import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;

import java.util.List;

public class App2_consumer {
    public static void main(String[] args) throws Exception{
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("consumer_group1");        consumer.setNamesrvAddr("192.168.25.129:9876");
        consumer.setMessageModel(MessageModel.BROADCASTING);
        consumer.subscribe("TopicTest","*");
        consumer.registerMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list, ConsumeConcurrentlyContext consumeConcurrentlyContext) {
                if (list != null) {
                    for (MessageExt messageExt : list) {
                        byte[] body = messageExt.getBody();
                        String message = new String(body);
                        System.out.println(message+"%s%n"+messageExt.getTopic()+"%s %n"+messageExt.getTags());
                    }
                    return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                }
                return ConsumeConcurrentlyStatus.RECONSUME_LATER;
            }
        });

        consumer.start();
    }
}
