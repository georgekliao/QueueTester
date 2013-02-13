package com.sailthru.queuetester.queue;

/**
 *
 * @author georgekliao
 */
public interface IQueue {

    public void publish(Object obj);

    public void subscribe();

    public void subscribeLateAck();

    public Object pop();

    public Object popLateAck();

    public boolean ack(Object obj);
}
