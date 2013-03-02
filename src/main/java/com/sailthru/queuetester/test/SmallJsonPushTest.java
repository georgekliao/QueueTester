/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sailthru.queuetester.test;

import com.sailthru.queuetester.queue.IQueue;

/**
 *
 * @author georgekliao
 */
public class SmallJsonPushTest extends AbstractTest {

    IQueue queue;

    public SmallJsonPushTest(IQueue queue) {
        this.queue = queue;
    }

    public void run() {
        for(int i = 0; i < 1000; i++) {
            queue.push(this.getMessage(i));
        }
    }

    public String getMessage(int i) {
        return "{ 'test' : 'thread_" + Thread.currentThread().getId() + ", 'test2' : " + i + " }";
    }
}
