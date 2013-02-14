package com.sailthru.queuetester.queue.rabbitmq;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import com.sailthru.queuetester.queue.redis.*;
import com.sailthru.queuetester.queue.IQueue;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import redis.clients.jedis.Jedis;

/**
 *
 * @author georgekliao
 */
public class RabbitQueue implements IQueue {

    private final String exchangeName;
    private final String queueName;
    private final String routingKey;
    private Channel channel;

    public RabbitQueue(String queueName) throws Exception {
        AMQP.BasicProperties.Builder bob = new AMQP.BasicProperties.Builder();
        AMQP.BasicProperties minBasic = bob.build();
        AMQP.BasicProperties minPersistentBasic = bob.deliveryMode(2).build();
        AMQP.BasicProperties persistentBasic = bob.priority(0).contentType("application/octet-stream").build();
        AMQP.BasicProperties persistentTextPlain = bob.contentType("text/plain").build();

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection conn = factory.newConnection();

        this.queueName = queueName;
        this.exchangeName = queueName + "_exchange";
        this.routingKey = queueName + "_routingKey";

        this.channel = conn.createChannel();
        channel.exchangeDeclare(exchangeName, "direct", true);
        channel.queueDeclare(queueName, true, false, false, null);
        channel.queueBind(queueName, exchangeName, routingKey);
    }

    @Override
    public void publish(Object obj) {
        byte[] bytes = obj.toString().getBytes();
        try {
            channel.basicPublish(exchangeName, routingKey, null, bytes);
        } catch (IOException ex) {
            Logger.getLogger(RabbitQueue.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void subscribe() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void subscribeLateAck() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Object pop() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Object popLateAck() {
        boolean autoAck = false;
        try {
            channel.basicConsume(queueName, autoAck, "myConsumerTag",
                    new DefaultConsumer(channel) {
                        @Override
                        public void handleDelivery(String consumerTag,
                                Envelope envelope,
                                AMQP.BasicProperties properties,
                                byte[] body)
                                throws IOException {
                            long deliveryTag = envelope.getDeliveryTag();
                            // (process the message components here ...)
                            System.out.println("Test: " + new String(body));
                            channel.basicAck(deliveryTag, false);
                        }
                    });
        } catch (IOException ex) {
            Logger.getLogger(RabbitQueue.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    public boolean ack(Object obj) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void unack(Object obj) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
