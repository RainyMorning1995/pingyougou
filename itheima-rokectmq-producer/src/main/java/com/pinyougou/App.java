package com.pinyougou;

import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.apache.rocketmq.remoting.exception.RemotingException;

import java.io.UnsupportedEncodingException;

public class App {
    public static void main(String[] args) throws MQClientException, UnsupportedEncodingException, RemotingException, InterruptedException, MQBrokerException {
        DefaultMQProducer producer = new DefaultMQProducer("producer_cluster_group1");
        producer.setNamesrvAddr("192.168.25.129:9876");
        producer.start();
        for (int i = 0; i < 100; i++) {
            Message msg = new Message(
                    "TopicTest",
                    "Tags",
                    ("你好："+i).getBytes(RemotingHelper.DEFAULT_CHARSET)
            );
            SendResult send = producer.send(msg);
            System.out.printf("%s%n", send);
        }
        producer.shutdown();
    }
}
