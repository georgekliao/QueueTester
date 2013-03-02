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
public class BasicSubscribeTest extends AbstractTest {

    IQueue queue;

    public BasicSubscribeTest(IQueue queue) {
        this.queue = queue;
    }

    public void run() {
        queue.subscribe();
    }
}
