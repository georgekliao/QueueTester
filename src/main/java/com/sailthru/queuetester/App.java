package com.sailthru.queuetester;

import com.sailthru.queuetester.queue.IQueue;
import com.sailthru.queuetester.queue.kafka.KafkaQueue;
import com.sailthru.queuetester.queue.rabbitmq.RabbitQueue;
import com.sailthru.queuetester.queue.redis.RedisQueue;

public class App {

    public static void main(String[] args) throws Exception {
//        RedisQueue redisQueue = new RedisQueue("test_queue");
//        IQueue queue = new RabbitQueue("test_queue");
        IQueue queue = new KafkaQueue("test_queue");

        for (int i = 0; i < 100; i++) {
            System.out.println("Publishing " + i);
            queue.publish(i);
        }

        System.out.println("Done publishing");
        /*
        Object i = queue.pop();
        while(i != null) {
            System.out.println("Got: " + i);
            i = queue.pop();
        }
        */

        queue.popLateAck();
    }
}
