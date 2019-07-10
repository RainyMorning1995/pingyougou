package com.pinyougou;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@ContextConfiguration("classpath:spring-consumer.xml")
public class ConsumerTest {

    @Test
    public void test() throws Exception{
        Thread.sleep(1000000);
    }
}
