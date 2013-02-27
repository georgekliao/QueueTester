package com.sailthru.queuetester.queue.redis;

import com.sailthru.queuetester.queue.IQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import redis.clients.jedis.Jedis;

/**
 *
 * @author georgekliao
 */
public class RedisQueue implements IQueue {

    private final String queueName;
    private final long sleepTime;
    private Jedis jedis = new Jedis("localhost");

    public RedisQueue(String queueName) {
        this.queueName = queueName;
        this.sleepTime = 1000;
    }

    @Override
    public void push(Object obj) {
        System.out.println("Pushed: " + obj);
        this.jedis.lpush(queueName, obj.toString());
    }

    public void subscribe() {
        while (true) {
            Object obj = this.jedis.rpop(queueName);

            if (obj != null) {
                System.out.println("Popped: " + obj);
            } else {
                try {
                    Thread.sleep(this.sleepTime);
                } catch (InterruptedException ex) {
                    Logger.getLogger(RedisQueue.class.getName()).log(Level.SEVERE, "Subscribe sleep interrupted!", ex);
                }
            }
        }
    }

    public Object pop() {
        Object obj = this.jedis.rpop(queueName);
        System.out.println("Popped: " + obj);
        return obj;
    }
}
