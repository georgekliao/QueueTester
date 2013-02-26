package com.sailthru.queuetester.queue.kafka;

import com.google.common.collect.ImmutableMap;
import com.sailthru.queuetester.queue.IQueue;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import kafka.javaapi.producer.Producer;
import kafka.javaapi.producer.ProducerData;
import kafka.message.Message;
import kafka.message.MessageAndMetadata;
import kafka.producer.ProducerConfig;

/**
 *
 * @author georgekliao
 */
public class KafkaQueue implements IQueue {

    private final Producer<String, String> producer;
    private final String queueName;
    List<KafkaStream<Message>> streams;

    public KafkaQueue(String queueName) {
        Properties producerProps = new Properties();
        producerProps.put("zk.connect", "127.0.0.1:2181");
        producerProps.put("serializer.class", "kafka.serializer.StringEncoder");
        ProducerConfig config = new ProducerConfig(producerProps);
        producer = new Producer<String, String>(config);

        Properties consumerProps = new Properties();
        consumerProps.put("zk.connect", "127.0.0.1:2181");
        consumerProps.put("zk.connectiontimeout.ms", "1000000");
        consumerProps.put("groupid", "test_group");

        ConsumerConfig consumerConfig = new ConsumerConfig(consumerProps);
        ConsumerConnector consumerConnector = Consumer.createJavaConsumerConnector(consumerConfig);

        // create 4 partitions of the stream for topic “test”, to allow 4 threads to consume
        Map<String, List<KafkaStream<Message>>> topicMessageStreams =
                consumerConnector.createMessageStreams(ImmutableMap.of(queueName, 1));
        this.streams = topicMessageStreams.get(queueName);

        this.queueName = queueName;
    }

    @Override
    public void publish(Object obj) {
        ProducerData<String, String> data = new ProducerData<String, String>(queueName, obj.toString());
        producer.send(data);
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
        ExecutorService executor = Executors.newFixedThreadPool(4);
        for (final KafkaStream<Message> stream : streams) {
            executor.submit(new Runnable() {
                public void run() {
                    for (Object obj : stream) {
                        MessageAndMetadata msgAndMetadata = (MessageAndMetadata) obj;

                        Message message = (Message) msgAndMetadata.message();
                        ByteBuffer payload = message.payload();
                        byte[] bytes = new byte[payload.limit()];
                        payload.get(bytes);
                        try {
                            System.out.println(new String(bytes, "UTF-8"));
                        } catch (UnsupportedEncodingException ex) {
                            Logger.getLogger(KafkaQueue.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            });
        }

        return null;
    }

    public boolean ack(Object obj) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void unack(Object obj) {
    }
}
