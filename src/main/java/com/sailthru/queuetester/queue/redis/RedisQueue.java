package com.sailthru.queuetester.queue.redis;

import com.sailthru.queuetester.queue.IQueue;
import redis.clients.jedis.Jedis;

/**
 *
 * @author georgekliao
 */
public class RedisQueue implements IQueue {

    private final String queueName;
    private final String processingQueueName;
    private Jedis jedis = new Jedis("localhost");

    public RedisQueue(String queueName) {
        this.queueName = queueName;
        this.processingQueueName = queueName + "_processing";
    }

    @Override
    public void publish(Object obj) {
        this.jedis.lpush(queueName, obj.toString());
    }

    public void subscribe() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void subscribeEarlyAck() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Object pop() {
        return this.jedis.rpoplpush(queueName, processingQueueName);
    }

    public Object popEarlyAck() {
        return this.jedis.rpop(queueName);
    }

    public boolean ack(Object obj) {
        return this.jedis.lrem(this.processingQueueName, -1, obj.toString()) == 1 ? true : false;
    }

    public void unack(Object obj) {
        this.jedis.rpoplpush(processingQueueName, queueName);
    }

}
