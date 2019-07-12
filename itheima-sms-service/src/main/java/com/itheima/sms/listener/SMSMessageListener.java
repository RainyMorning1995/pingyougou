package com.itheima.sms.listener;

import com.alibaba.fastjson.JSON;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;
import com.itheima.sms.utils.SmsUtil;

import java.util.List;
import java.util.Map;

public class SMSMessageListener implements MessageListenerConcurrently {
    @Override
    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext consumeConcurrentlyContext) {
        try {
            if(msgs!=null){
                for (MessageExt msg : msgs) {
                    byte[] body = msg.getBody();
                    String ss = new String(body);
                    //获取到相关信息
                    Map<String,String> map = JSON.parseObject(ss, Map.class);//有签名和其他的信息
                    SmsUtil.sendSms(map);
                }
            }
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ConsumeConcurrentlyStatus.RECONSUME_LATER;
        }


    }
}
