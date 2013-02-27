package com.sailthru.queuetester.queue;

/**
 *
 * @author georgekliao
 */
public interface IQueue {

    public void push(Object obj);

    public Object pop();

    public void subscribe();
}
