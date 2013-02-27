package com.sailthru.queuetester;

import com.sailthru.queuetester.queue.IQueue;
import com.sailthru.queuetester.queue.kafka.KafkaQueue;
import com.sailthru.queuetester.queue.rabbitmq.RabbitQueue;
import com.sailthru.queuetester.queue.redis.RedisQueue;
import com.sailthru.queuetester.test.ManyJsonSubscribeTest;
import com.sailthru.queuetester.test.AbstractTest;

public class App {

    public static void main(String[] args) throws Exception {
        IQueue queue = new RabbitQueue("test_queue");
        AbstractTest test = new ManyJsonSubscribeTest(queue);
        test.execute();
    }
}
