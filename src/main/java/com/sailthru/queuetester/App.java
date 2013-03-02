package com.sailthru.queuetester;

import com.sailthru.queuetester.queue.IQueue;
import com.sailthru.queuetester.queue.activemq.ActivemqQueue;
import com.sailthru.queuetester.queue.kafka.KafkaQueue;
import com.sailthru.queuetester.queue.rabbitmq.RabbitQueue;
import com.sailthru.queuetester.queue.redis.RedisQueue;
import com.sailthru.queuetester.test.BasicSubscribeTest;
import com.sailthru.queuetester.test.AbstractTest;
import com.sailthru.queuetester.test.SmallJsonPushTest;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

public class App {

    public static void main(String[] args) throws Exception {

        ExecutorService executor = Executors.newFixedThreadPool(4);

        for (int i = 0; i < 4; i++) {
            executor.submit(new Runnable() {
                public void run() {
                    App app = new App();
                    app.runTest();
                }
            });
        }
    }

    public void runTest() {
        try {
            IQueue queue = new RabbitQueue("test_queue");
//            AbstractTest test = new SmallJsonPushTest(queue);
        AbstractTest test = new BasicSubscribeTest(queue);
            test.execute();
        } catch (Exception ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
