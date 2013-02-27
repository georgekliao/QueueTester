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
public class ManyJsonSubscribeTest extends AbstractTest {

    IQueue queue;

    public ManyJsonSubscribeTest(IQueue queue) {
        this.queue = queue;
    }

    public void run() {
        for(int i = 0; i < 1000; i++) {
            queue.push(this.getMessage(i));
        }

        queue.subscribe();
    }

    public String getMessage(int i) {
        return "{ 'test' : 'test2', 'test3' : " + i + " }";
    }
}
