package com.sailthru.queuetester;

import com.sailthru.queuetester.queue.redis.RedisQueue;

public class App {

    public static void main(String[] args) {
        RedisQueue redisQueue = new RedisQueue("test_queue");

        for (int i = 0; i < 100; i++) {
            System.out.println("Publishing " + i);
            redisQueue.publish(i);
        }

        System.out.println("Done publishing");
        
        Object i = redisQueue.pop();
        while(i != null) {
            System.out.println("Got: " + i);
            i = redisQueue.pop();
        }
    }
}
