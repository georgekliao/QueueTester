package com.sailthru.queuetester;

import com.sailthru.queuetester.queue.redis.RedisQueue;

public class App {

    public static void main(String[] args) {
        RedisQueue redisQueue = new RedisQueue();

        for (int i = 0; i < 100; i++) {
            System.out.println("Publishing " + i);
            redisQueue.publish(i);
        }

        System.out.println("Done publishing");

        redisQueue.subscribe();
    }
}
