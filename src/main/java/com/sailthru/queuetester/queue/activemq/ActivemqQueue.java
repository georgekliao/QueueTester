package com.sailthru.queuetester.queue.activemq;

import com.sailthru.queuetester.queue.IQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQQueue;

/**
 *
 * @author georgekliao
 */
public class ActivemqQueue implements IQueue {

    MessageProducer producer;
    MessageConsumer consumer;
    Session session;

    public ActivemqQueue(String queueName) throws JMSException {
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:6166");
        Connection connection = connectionFactory.createConnection();
        connection.start();

        // Create a Session
        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        // Create the destination (Topic or Queue)
        Destination destination = session.createQueue(queueName);

        producer = session.createProducer(new ActiveMQQueue(queueName));
        producer.setDeliveryMode(DeliveryMode.PERSISTENT);

        consumer = session.createConsumer(destination);
    }

    public void push(Object obj) {
        try {
            TextMessage message = session.createTextMessage(obj.toString());
            System.out.println("Sending: " + obj.toString());
            producer.send(message);
        } catch (JMSException ex) {
            Logger.getLogger(ActivemqQueue.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Object pop() {
        try {
            Message message = consumer.receive();

            if (message instanceof TextMessage) {
                TextMessage textMessage = (TextMessage) message;
                String text = textMessage.getText();
                System.out.println("Received: " + text);

                return text;
            } else {
                System.out.println("Received: " + message);
            }
        } catch (Exception ex) {
            Logger.getLogger(ActivemqQueue.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public void subscribe() {
        try {
            consumer.setMessageListener(new MessageListener() {
                public void onMessage(Message message) {

                    if (message instanceof TextMessage) {
                        TextMessage textMessage = (TextMessage) message;
                        String text;
                        try {
                            text = textMessage.getText();
                            System.out.println("Received: " + text);
                        } catch (JMSException ex) {
                            Logger.getLogger(ActivemqQueue.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    } else {
                        System.out.println("Received: " + message);
                    }
                }
            });
        } catch (JMSException ex) {
            Logger.getLogger(ActivemqQueue.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
