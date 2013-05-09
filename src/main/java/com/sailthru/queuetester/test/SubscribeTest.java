package com.sailthru.queuetester.test;

/**
 *
 * @author georgekliao
 */
public class SubscribeTest extends AbstractTest {
    public void run() {
        getQueue().subscribe();
    }        
}
