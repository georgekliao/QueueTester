package com.sailthru.queuetester.queue;

import com.sailthru.queuetester.queue.activemq.ActivemqQueue;
import com.sailthru.queuetester.queue.kafka.KafkaQueue;
import com.sailthru.queuetester.queue.rabbitmq.RabbitQueue;
import com.sailthru.queuetester.queue.redis.RedisQueue;

/**
 *
 * @author maxenglander
 */
public class QueueFactory {
    public static IQueue create(String type, String name) throws Exception {
        if(type.equals("amq")) {
            return new ActivemqQueue(name);
        }
        if(type.equals("kafka")) {
            return new KafkaQueue(name);
        }
        if(type.equals("redis")) {
            return new RedisQueue(name);
        }
        if(type.equals("rmq")) {
            return new RabbitQueue(name);
        }
        throw new IllegalArgumentException("Unrecognized type: " + type);
    }
}
