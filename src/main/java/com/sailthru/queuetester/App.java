package com.sailthru.queuetester;

import com.sailthru.queuetester.queue.IQueue;
import com.sailthru.queuetester.queue.QueueFactory;
import com.sailthru.queuetester.test.AbstractTest;
import com.sailthru.queuetester.test.TestFactory;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;

public class App {                
    public static void main(String[] args) throws Exception {        
        Options options = new Options(); 
        options.addOption("c", true, "concurrency [default 4]");
        options.addOption("h", true, "host [default localhost]");
        options.addOption("n", true, "number of messages [default 1000]");        
        options.addOption("q", true, "queue type [kafka, redis, amq, rmq]");
        options.addOption("s", true, "message size [default 1024]");
        options.addOption("t", true, "test type [sub, pub]");        
        
        CommandLineParser commandLineParser = new BasicParser();
        CommandLine commandLine = commandLineParser.parse(options, args);
        
        int concurrency = Integer.parseInt(commandLine.getOptionValue("c", "4"));
        final String host = commandLine.getOptionValue("h", "localhost");
        final String queueType = commandLine.getOptionValue("q", "amq");
        final int messageSize = Integer.parseInt(commandLine.getOptionValue("s", "1024"));
        final int numMessages = Integer.parseInt(commandLine.getOptionValue("n", "1000"));        
        final String testType = commandLine.getOptionValue("t", "pub");
        
        int availableProcessors = Runtime.getRuntime().availableProcessors();
        ExecutorService executor = Executors.newFixedThreadPool(availableProcessors <= concurrency ? availableProcessors : concurrency);        
        
        for (int i = 0; i < concurrency; i++) {
            executor.submit(new Runnable() {
                public void run() {
                    try {
                        IQueue queue = QueueFactory.create(queueType, host, "test_queue");
                        AbstractTest test = TestFactory.create(testType, numMessages, messageSize);
                        test.setQueue(queue);                    
                        test.execute();                        
                    } catch (Exception ex) {
                        Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
                    }                    
                }
            });
        }
        
        // See http://stackoverflow.com/questions/1250643/how-to-wait-for-all-threads-to-finish-using-executorservice
        executor.shutdown();
        executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);        
    }
}
