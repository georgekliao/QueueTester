package com.sailthru.queuetester.queue;

/**
 *
 * @author georgekliao
 */
public interface IQueue {

    public void publish(Object obj);

    public void subscribe();

    public void subscribeEarlyAck();

    public Object pop();

    public Object popEarlyAck();

    public boolean ack(Object obj);
}
