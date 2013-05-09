/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sailthru.queuetester.test;

import com.sailthru.queuetester.queue.IQueue;
import java.util.Date;

/**
 *
 * @author georgekliao
 */
public abstract class AbstractTest {
    private IQueue queue;
    private Date startTime;
    private Date endTime;

    public void execute() {
        System.out.println("Starting test");

        this.startTime = new Date();
        run();
        this.endTime = new Date();

        long testTime = this.endTime.getTime() - this.startTime.getTime();

        System.out.println("Test took: " + testTime + " milliseconds");
    }
    
    public IQueue getQueue() {
        return this.queue;
    }

    public abstract void run();
    
    public void setQueue(IQueue queue) {
        this.queue = queue;
    }
}
