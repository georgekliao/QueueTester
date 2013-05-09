package com.sailthru.queuetester.test;

/**
 *
 * @author georgekliao
 */
public class PublishTest extends AbstractTest {    
    private int messageSize;
    private int numMessages;
    
    public PublishTest(int numMessages, int messageSize) {
        this.numMessages = numMessages;
        this.messageSize = messageSize;
    }
    
    public void run() {
        if(numMessages > 0) {
            for(int i = 0; i < numMessages; i++) {
                getQueue().push(this.makeMessage(i));
            }
        } else {
            int i = 0;
            while(true) {                
                getQueue().push(this.makeMessage(i));
                i++;
            }
        }
    }

    public String makeMessage(int i) {
        long threadId = Thread.currentThread().getId();
        String format = "{ 'id' : " + i + ", 'thread' : " + threadId + ", 'data' : '%s' }";
        char[] data = new char[messageSize - 2 - format.length()];
        for(i = 0; i < data.length; i++) {
            data[i] = 'x';
        }
        return String.format(format, new String(data));        
    }
}
