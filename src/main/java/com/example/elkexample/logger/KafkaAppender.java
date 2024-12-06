package com.example.elkexample.logger;

import ch.qos.logback.core.AppenderBase;
import ch.qos.logback.core.encoder.Encoder;
import lombok.Getter;
import lombok.Setter;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

@Setter
@Getter
public class KafkaAppender<E> extends AppenderBase<E> {

    private String topic = null;
    private String brokers = null;


    private Encoder<E> encoder = null;
    private KafkaProducer<String, byte[]> producer = null;


    @Override
    public void start() {
        if (this.encoder == null) {
            addError("No encoder set for the appender named [" + name + "].");
            return;
        }
        if (topic == null || brokers == null) {
            addError("Topic or brokers cannot be null.");
            return;
        }

        Properties props = new Properties();
        props.put("bootstrap.servers", brokers);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.ByteArraySerializer");

        ProducerRecord<String, byte[]> record = new ProducerRecord<>(
                topic,
                "{\n\tKafka start\n}".getBytes()
        );

        producer = new KafkaProducer<>(props);
        producer.send(record);
        producer.flush();

        super.start();
    }


    @Override
    public void append(E eventObject) {
        if (encoder == null) {
            throw new IllegalStateException("Encoder must not be null.");
        }

        byte[] bytes = encoder.encode(eventObject);
        ProducerRecord<String, byte[]> record = new ProducerRecord<>(topic, bytes);
        if (producer != null) {
            producer.send(record);
        }
    }

    @Override
    public void stop() {
        super.stop();
        if (producer != null) {
            producer.close();
        }
    }
}
