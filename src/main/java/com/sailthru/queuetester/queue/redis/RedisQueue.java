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
    private final String processingQueueName;
    private Jedis jedis = new Jedis("localhost");

    public RedisQueue(String queueName) {
        this.queueName = queueName;
        this.sleepTime = 1000;
        this.processingQueueName = queueName + "_processing";
    }

    @Override
    public void publish(Object obj) {
        this.jedis.lpush(queueName, obj.toString());
    }

    public void subscribe() {
        throw new UnsupportedOperationException("Not supported yet.");
        
//        while(true) {
//            Object obj = this.pop();
//            
//            if (obj != null) {
//                
//            } else {
//                try {
//                    Thread.sleep(this.sleepTime);
//                } catch (InterruptedException ex) {
//                    Logger.getLogger(RedisQueue.class.getName()).log(Level.SEVERE, "Subscribe sleep interrupted!", ex);
//                }
//            }
//        }
    }

    public void subscribeLateAck() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Object pop() {
        return this.jedis.rpop(queueName);
    }

    public Object popLateAck() {
        return this.jedis.rpoplpush(queueName, processingQueueName);
    }

    public boolean ack(Object obj) {
        return this.jedis.lrem(this.processingQueueName, -1, obj.toString()) == 1 ? true : false;
    }

    public void unack(Object obj) {
        this.jedis.rpoplpush(processingQueueName, queueName);
    }

}
