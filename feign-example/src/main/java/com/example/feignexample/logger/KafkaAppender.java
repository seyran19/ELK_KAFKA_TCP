package com.example.feignexample.logger;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;
import ch.qos.logback.core.encoder.Encoder;
import lombok.Getter;
import lombok.Setter;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.nio.charset.StandardCharsets;
import java.util.Date;
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


//    @Override
//    public void append(E eventObject) {
//        if (encoder == null) {
//            throw new IllegalStateException("Encoder must not be null.");
//        }
//
//        byte[] bytes = encoder.encode(eventObject);
//        ProducerRecord<String, byte[]> record = new ProducerRecord<>(topic, bytes);
//        if (producer != null) {
//            producer.send(record);
//        }
//    }

    @Override
    public void append(E eventObject) {
        if (!(eventObject instanceof ILoggingEvent)) {
            throw new IllegalArgumentException("Event object must be an instance of ILoggingEvent.");
        }

        ILoggingEvent loggingEvent = (ILoggingEvent) eventObject;

        // Достаем traceId и spanId из MDC
        String traceId = loggingEvent.getMDCPropertyMap().getOrDefault("traceId", "-");
        String spanId = loggingEvent.getMDCPropertyMap().getOrDefault("spanId", "-");

        // Добавляем traceId и spanId к логируемому событию (можно модифицировать encoder, если нужно)
        String logMessage = String.format(
                "{ \"timestamp_logger\": \"%s\", \"level\": \"%s\", \"message\": \"%s\", " +
                        "\"thread\": \"%s\", \"traceId\": \"%s\", \"spanId\": \"%s\" }",
                new Date(loggingEvent.getTimeStamp()),
                loggingEvent.getLevel(),
                loggingEvent.getFormattedMessage(),
                loggingEvent.getThreadName(),
                traceId,
                spanId
        );

        // Кодируем сообщение
        byte[] bytes = logMessage.getBytes(StandardCharsets.UTF_8);

        // Создаем запись для Kafka
        ProducerRecord<String, byte[]> record = new ProducerRecord<>(topic, bytes);

        // Отправляем в Kafka
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
