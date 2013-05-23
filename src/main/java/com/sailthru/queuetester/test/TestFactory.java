package com.sailthru.queuetester.test;

/**
 *
 * @author maxenglander
 */
public class TestFactory {
    public static AbstractTest create(String testType, int numMessages, int messageSize) {
        if(testType.equals("pub")) {
            return new PublishTest(numMessages, messageSize);
        }
        if(testType.equals("sub")) {
            return new SubscribeTest();
        }
        
        throw new IllegalArgumentException("Unrecognized test type: " + testType);
    }
}
